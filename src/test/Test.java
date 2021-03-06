import cn.cnic.autocheck.model.CronJob;
import cn.cnic.autocheck.service.EmailService;
import cn.cnic.autocheck.utils.HttpUtil;
import cn.cnic.autocheck.utils.XMLReader;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.DocumentException;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by liuang on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class Test {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private EmailService emailService;

    @org.junit.Test
    public void test() throws InterruptedException, IOException, DocumentException {
        String xmlPath = "D:\\workspace\\cnicCheck\\src\\main\\webapp\\config\\cron.xml";
        List<CronJob> cronJobs = XMLReader.readElements("cron", xmlPath);
        for (CronJob cronJob : cronJobs) {
            System.out.println(cronJob.getId() + "-" + cronJob.getCheckInTime() + "-" + cronJob.getCheckInTimeFrom());
        }
        XMLReader.removeElement("huangwei@cnic.cn", xmlPath);
        cronJobs = XMLReader.readElements("cron", xmlPath);
        for (CronJob cronJob : cronJobs) {
            System.out.println(cronJob.getId() + "-" + cronJob.getCheckInTime() + "-" + cronJob.getCheckInTimeFrom());
        }
        CronJob cronJob = new CronJob();
        cronJob.setId("huangwei@cnic.cn");
        cronJob.setEmail("huangwei@cnic.cn");
        cronJob.setCheckInTimeFrom("9:00");
        cronJob.setCheckInTimeTo("9:30");
        cronJob.setCheckOutTimeFrom("18:00");
        cronJob.setCheckInTimeTo("19:00");
        XMLReader.addElement(cronJob, xmlPath);
        cronJobs = XMLReader.readElements("cron", xmlPath);
        for (CronJob job : cronJobs) {
            System.out.println(job.getId() + "-" + job.getCheckInTime() + "-" + job.getCheckInTimeFrom());
        }
    }

    @org.junit.Test
    public void testHttp() throws IOException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(HttpUtil.get("http://www.easybots.cn/api/holiday.php?d=" + sdf.format(calendar.getTime())));
        JSONObject jsonObject = HttpUtil.get("http://www.easybots.cn/api/holiday.php?d=" + sdf.format(calendar.getTime()));
        System.out.println(jsonObject.getString(sdf.format(calendar.getTime())).equals("0"));
//        System.out.println(HttpUtil.get("http://159.226.29.10/CnicCheck/CheckServlet?weidu=39.9794962420&jingdu=116.3293553275&type=checkin" +
//                "&token=b85e7d7ec3595b59fc3dc89c8337e56d"));
    }

    @org.junit.Test
    public void testEmail() {
        emailService.sendEmail("1231", "liuang@cnic.cn", "123123");
    }
}
