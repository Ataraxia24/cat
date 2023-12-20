package cn.service.impl;

import cn.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;

@Service
public class SendMailServiceImpl implements SendMailService {
    //boot整合mail, 其协议等都写好了, 只需导入坐标, 设置配置, 创建对象, 设置参数 即可

    @Autowired
    private JavaMailSender javaMailSender;      //发送邮件对象

    //发件人
    @Value("${spring.mail.username}")
    private String from = "2930620160@qq.com";

    @Override
    public boolean sendMail(String to, String context, String title) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        //设置发送邮件的信息
//        message.setFrom(from);
//        message.setTo(to);
//        message.setSubject(title);
//        message.setText(context);
//        javaMailSender.send(message);                           //发送邮件

        //多部件邮件, 比上面更丰富的设置 如超链接, 图片等
        try {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();       //创建多部件邮件

            //multipart:true 开启多部件, 以附件形式时, 属于多部件
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,true);

        //设置发送邮件的信息  格式一样
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(title);
            messageHelper.setText(context,true);        //后面一个true用于转换成链接

            javaMailSender.send(mailMessage);                           //发送邮件

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
