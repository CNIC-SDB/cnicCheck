package cn.cnic.autocheck.service.job;

import cn.cnic.autocheck.model.CronJob;
import cn.cnic.autocheck.service.EmailService;
import cn.cnic.autocheck.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Created by liuang on 2017/5/27.
 */
public class CheckThread implements Runnable {
    private CronJob job;
    private String type;
    private EmailService emailService;
    private Logger logger = LoggerFactory.getLogger(CheckThread.class);

    public CheckThread(CronJob job, String type, EmailService emailService) {
        this.job = job;
        this.type = type;
        this.emailService = emailService;
    }

    public void run() {
        //发送http请求
        String url = "http://159.226.29.10/CnicCheck/CheckServlet?weidu=39.9794962420&jingdu=116.3293553275&type=" + type +
                "&token=" + job.getCode();
        boolean isCheck = false;
        try {
            isCheck = HttpUtil.get(url);
        } catch (IOException e) {
            logger.error("发送打卡请求失败:" + job.getId(), e);
        }
        //发送提醒
        if (isCheck) {
            logger.info(job.getId() + "打卡成功：" + type);
            //发送成功打卡提示
            emailService.sendEmail("悄悄滴打枪地不要", job.getEmail(), type + " 打卡成功！" + new Date());
        } else {
            logger.info(job.getId() + "打卡失败：" + type);
            //发送失败打卡提示
            emailService.sendEmail("快自己打卡", job.getEmail(), type + " 打卡失败！" + new Date());
        }
    }
}
