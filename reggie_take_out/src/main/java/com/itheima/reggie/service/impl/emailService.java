package com.itheima.reggie.service.impl;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class emailService {
private Integer code;
@Value("${spring.mail.username}")
private String from;

    private JavaMailSender javaMailSender = new JavaMailSenderImpl();
public String email(String to){
    code = RandomUtil.randomInt(100000,999999);
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(from);
    simpleMailMessage.setTo(to);
    simpleMailMessage.setSubject("test");
    simpleMailMessage.setText("Code:"+code.toString());
    javaMailSender.send(simpleMailMessage);
    return code.toString();
}

}
