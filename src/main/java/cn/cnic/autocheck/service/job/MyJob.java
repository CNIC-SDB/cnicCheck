package cn.cnic.autocheck.service.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by liuang on 2017/5/25.
 */
public class MyJob {
    Logger logger = LoggerFactory.getLogger(MyJob.class);

    public void doSomething() {
        logger.info("do something---" + new Date());
    }
}
