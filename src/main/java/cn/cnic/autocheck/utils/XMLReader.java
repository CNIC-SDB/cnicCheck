package cn.cnic.autocheck.utils;

import cn.cnic.autocheck.model.CronJob;
import cn.cnic.autocheck.service.job.MyJob;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.dom.DOMDocument;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuang on 2017/5/26.
 */
public class XMLReader {

    public static List<CronJob> readElements(String tagName, String xmlPath) throws FileNotFoundException, DocumentException {
        InputStream inputStream = new FileInputStream(xmlPath);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements(tagName);
        List<CronJob> myJobs = new ArrayList<CronJob>(16);
        for (Element element : elements) {
            String id = element.attributeValue("id");
            CronJob cronJob = new CronJob();
            cronJob.setId(id);
            cronJob.setEmail(element.attributeValue("email"));
            cronJob.setCode(element.attributeValue("code"));
            cronJob.setCheckInTimeFrom(element.attributeValue("checkInTimeFrom"));
            cronJob.setCheckInTimeTo(element.attributeValue("checkInTimeTo"));
            cronJob.setCheckOutTimeFrom(element.attributeValue("checkOutTimeFrom"));
            cronJob.setCheckOutTimeTo(element.attributeValue("checkOutTimeTo"));
            myJobs.add(cronJob);
        }
        return myJobs;
    }

    public static void addElement(CronJob job, String xmlPath) throws IOException, DocumentException {
        InputStream inputStream = new FileInputStream(xmlPath);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        Element element = (Element) rootElement.selectSingleNode("//cron[@id='" + job.getId() + "']");
        if (element == null)
            element = rootElement.addElement("cron");
        element.addAttribute("id", job.getId());
        element.addAttribute("email", job.getEmail());
        element.addAttribute("code", job.getCode());
        element.addAttribute("checkInTimeFrom", job.getCheckInTimeFrom());
        element.addAttribute("checkInTimeTo", job.getCheckInTimeTo());
        element.addAttribute("checkOutTimeFrom", job.getCheckOutTimeFrom());
        element.addAttribute("checkOutTimeTo", job.getCheckOutTimeTo());
        writeToFile(document, xmlPath);
    }

    public static void removeElement(String id, String xmlPath) throws IOException, DocumentException {
        InputStream inputStream = new FileInputStream(xmlPath);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Element rootElement = document.getRootElement();
        Element element = (Element) rootElement.selectSingleNode("//cron[@id='" + id + "']");
        if (element != null)
            rootElement.remove(element);
        writeToFile(document, xmlPath);
    }

    public static void writeToFile(Document document, String xmlPath) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(xmlPath);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(outputStream, format);
        writer.write(document);
        writer.close();
    }
}
