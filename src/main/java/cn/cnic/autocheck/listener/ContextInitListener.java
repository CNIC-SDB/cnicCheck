package cn.cnic.autocheck.listener;

import cn.cnic.autocheck.model.CronJob;
import cn.cnic.autocheck.utils.XMLReader;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by liuang on 2017/5/26.
 */
public class ContextInitListener implements ServletContextListener {

    public static ServletContext context;
    public static ExecutorService pool = new ThreadPoolExecutor(5, 5, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(20));
    private Logger logger = LoggerFactory.getLogger(ContextInitListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        ContextInitListener.context = context;
        try {
            List<CronJob> jobs = XMLReader.readElements("cron", context.getRealPath("/") + "/config/cron.xml");
            context.setAttribute("jobs", jobs);
        } catch (FileNotFoundException e) {
            logger.error("配置文件找不到", e);
        } catch (DocumentException e) {
            logger.error("文件格式错误", e);
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        pool.shutdown();
    }
}
