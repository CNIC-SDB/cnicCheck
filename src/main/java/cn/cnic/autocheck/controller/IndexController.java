package cn.cnic.autocheck.controller;

import cn.cnic.autocheck.model.CronJob;
import cn.cnic.autocheck.utils.HttpUtil;
import cn.cnic.autocheck.utils.XMLReader;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @ResponseBody
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
    @ResponseBody
    public boolean addJob(CronJob job) throws IOException, DocumentException {
        XMLReader.addElement(job, context.getRealPath("/") + "/config/cron.xml");
        List<CronJob> jobs = XMLReader.readElements("cron", context.getRealPath("/") + "/config/cron.xml");
        context.setAttribute("jobs", jobs);
        return true;
    }

    @RequestMapping("/time")
    @ResponseBody
    public String getSystemTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    @RequestMapping("/check")
    @ResponseBody
    public boolean check(String id, String type) {
        List<CronJob> jobs = (List<CronJob>) context.getAttribute("jobs");
        for (CronJob job : jobs) {
            if (job.getId().equals(id)) {
                String url = "http://159.226.29.10/CnicCheck/CheckServlet?weidu=39.9794962420&jingdu=116.3293553275&type=" + type +
                        "&token=" + job.getCode();
                try {
                    JSONObject jsonObject = HttpUtil.get(url);
                    if (jsonObject != null || jsonObject.getString("success").equals("true"))
                        return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
}
