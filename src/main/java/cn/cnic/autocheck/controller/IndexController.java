package cn.cnic.autocheck.controller;

import cn.cnic.autocheck.model.CronJob;
import cn.cnic.autocheck.utils.XMLReader;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liuang on 2017/5/26.
 */
@Controller
public class IndexController {
    @Resource
    ServletContext context;

    @RequestMapping("/")
    public ModelAndView loadJobs() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/del")
    public String deleteJob(String id) throws IOException, DocumentException {
        List<CronJob> jobs = (List<CronJob>) context.getAttribute("jobs");
        Iterator<CronJob> iterator = jobs.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(id))
                iterator.remove();
        }
        XMLReader.removeElement(id, context.getRealPath("/") + "/config/cron.xml");
        return "index";
    }

    @RequestMapping("/add")
    public boolean addJob(CronJob job) throws IOException, DocumentException {
        XMLReader.addElement(job, context.getRealPath("/") + "/config/cron.xml");
        List<CronJob> jobs = XMLReader.readElements("cron", context.getRealPath("/") + "/config/cron.xml");
        context.setAttribute("jobs", jobs);
        return true;
    }
}
