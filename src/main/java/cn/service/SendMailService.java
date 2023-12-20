package cn.service;

import org.springframework.beans.factory.annotation.Autowired;

public interface SendMailService {
    boolean sendMail(String to, String text, String title);
}
