package com.besafx.app.component;

import com.besafx.app.config.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

@Component
public class QuickEmail {

    @Autowired
    private EmailSender emailSender;

    public void send(String subject, List<String> emails, String title, String subTitle, String body, String buttonLink, String buttonText) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("/mailTemplate/MESSAGE.html");
            String message = org.apache.commons.io.IOUtils.toString(classPathResource.getInputStream(), Charset.defaultCharset());
            message = message.replaceAll("MESSAGE_TITLE", title);
            message = message.replaceAll("MESSAGE_SUB_TITLE", subTitle);
            message = message.replaceAll("MESSAGE_BODY", body);
            message = message.replaceAll("MESSAGE_BUTTON_LINK", buttonLink);
            message = message.replaceAll("MESSAGE_BUTTON_TEXT", buttonText);
            emailSender.send(subject, message, emails).get();
        } catch (Exception ex) {

        }
    }
}
