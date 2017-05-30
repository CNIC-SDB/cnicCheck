package cn.cnic.autocheck.service.job;

import cn.cnic.autocheck.listener.ContextInitListener;
import cn.cnic.autocheck.model.CronJob;
import cn.cnic.autocheck.service.EmailService;
import cn.cnic.autocheck.utils.HttpUtil;
import cn.cnic.autocheck.utils.XMLReader;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by liuang on 2017/5/25.
 */
public class MyJob {
    private Logger logger = LoggerFactory.getLogger(MyJob.class);
    private ServletContext context;
    private EmailService emailService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private boolean isHoliday = false;
    public void doSomething() {
        context = ContextInitListener.context;
        Calendar calendar = Calendar.getInstance();

        //早上8点，计算新一天每个人的打卡时间，时间段内随机时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        if (hour == 8 && min == 0) {
            //判断是不是节假日
            try {
                JSONObject jsonObject = HttpUtil.get("http://www.easybots.cn/api/holiday.php?d=" + sdf.format(calendar.getTime()));
                if (jsonObject != null && jsonObject.getString(sdf.format(calendar.getTime())).equals("0"))
                    isHoliday = false;
                else
                    isHoliday = true;
            } catch (IOException e) {
                logger.error("判断节假日失败,默认今天上班", e);
                isHoliday = false;
            }
            if (isHoliday)
                return;
            try {
                List<CronJob> jobs = (List<CronJob>) context.getAttribute("jobs");
                for (CronJob job : jobs) {
                    job.setCheckInTime();
                    job.setCheckOutTime();
                }
            } catch (Exception e) {
                logger.error("计算新打卡时间失败", e);
            }
        }
        if (isHoliday) {
            logger.info("今天节假日！");
            return;
        }
        List<CronJob> jobs = (List<CronJob>) context.getAttribute("jobs");
        for (CronJob job : jobs) {
            String checkInTime = job.getCheckInTime();
            String checkOutTime = job.getCheckOutTime();
            if (StringUtils.isEmpty(checkInTime) || StringUtils.isEmpty(checkOutTime)) {
                //判断是不是节假日
                try {
                    JSONObject jsonObject = HttpUtil.get("http://www.easybots.cn/api/holiday.php?d=" + sdf.format(calendar.getTime()));
                    if (jsonObject != null && jsonObject.getString(sdf.format(calendar.getTime())).equals("0"))
                        isHoliday = false;
                    else
                        isHoliday = true;
                } catch (IOException e) {
                    logger.error("判断节假日失败,默认今天上班", e);
                    isHoliday = false;
                }
                if (isHoliday)
                    return;
                job.setCheckInTime();
                job.setCheckOutTime();
                checkInTime = job.getCheckInTime();
                checkOutTime = job.getCheckOutTime();
            }
            try {
                int checkHour = Integer.parseInt(checkInTime.split(":")[0]);
                int checkMin = Integer.parseInt(checkInTime.split(":")[1]);
                if (checkHour == hour && checkMin == min) {
                    logger.info(job.getId() + "上班打卡：" + hour + ":" + min);
                    //上班打卡
                    ContextInitListener.pool.submit(new CheckThread(job, "checkin", emailService));
                }
                checkHour = Integer.parseInt(checkOutTime.split(":")[0]);
                checkMin = Integer.parseInt(checkOutTime.split(":")[1]);
                if (checkHour == hour && checkMin == min) {
                    logger.info(job.getId() + "下班打卡：" + hour + ":" + min);
                    //下班打卡
                    ContextInitListener.pool.submit(new CheckThread(job, "checkout", emailService));
                }
            } catch (Exception e) {
                logger.error("打卡失败", e);
                continue;
            }
        }
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
