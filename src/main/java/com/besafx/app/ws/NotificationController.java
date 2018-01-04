package com.besafx.app.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/notifyOne", method = RequestMethod.POST)
    @ResponseBody
    public void notifyOne(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "receiver") String receiver) {
        notificationService.notifyOne(Notification.builder().code(code).title(title).message(message).date(new Date()).type(type).build(), receiver);
    }

    @RequestMapping(value = "/notifyAll", method = RequestMethod.POST)
    @ResponseBody
    public void notifyAll(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "type") String type) {
        notificationService.notifyAll(Notification.builder().code(code).title(title).message(message).date(new Date()).type(type).build());
    }

    @RequestMapping(value = "/notifyAllExceptMe", method = RequestMethod.POST)
    @ResponseBody
    public void notifyAllExceptMe(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "type") String type) {
        notificationService.notifyAllExceptMe(Notification.builder().code(code).title(title).message(message).date(new Date()).type(type).build());
    }
}
