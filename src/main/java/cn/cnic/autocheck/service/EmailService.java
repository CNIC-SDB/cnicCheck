package cn.cnic.autocheck.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liuang on 2017/5/27.
 */
@Service
public class EmailService {

    @Resource
    private JavaMailSender mailSender;
    @Resource
    private SimpleMailMessage message;
    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String subject, String to, String content) {
        message.setSubject(subject);
        message.setTo(to);
        message.setText(content);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            logger.error("发送邮件出错！", e);
        }
    }
}
