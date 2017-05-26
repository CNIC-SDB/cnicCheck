import cn.cnic.autocheck.model.CronJob;
import cn.cnic.autocheck.utils.XMLReader;
import org.dom4j.DocumentException;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
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
}
