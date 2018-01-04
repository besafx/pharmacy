package com.besafx.app.ws;

import com.besafx.app.entity.Person;
import com.besafx.app.service.PersonService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {

    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //Send to one destination
    public void notifyOne(Notification notification, String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional.ofNullable(auth).ifPresent(value -> notification.setSender(value.getName()));
        notification.setReceiver(username);
        messagingTemplate.convertAndSendToUser(username, "/queue/notify", notification);
        logger.info("Send notification to " + notification.getReceiver() + ": " + notification);
    }

    //Send to multiple destination
    public void notifyAll(Notification notification) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional.ofNullable(auth).ifPresent(value -> notification.setSender(value.getName()));
        Lists.newArrayList(personService.findAll())
                .stream()
                .forEach(person -> {
                    notification.setReceiver(person.getEmail());
                    messagingTemplate.convertAndSendToUser(person.getEmail(), "/queue/notify", notification);
                    logger.info("Send notification to " + notification.getReceiver() + ": " + notification);
                });
    }

    //Send to multiple destination except me
    public void notifyAllExceptMe(Notification notification) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person me = personService.findByEmail(auth == null ? "" : auth.getName());
        Optional.ofNullable(auth).ifPresent(value -> notification.setSender(value.getName()));
        Lists.newArrayList(personService.findAll())
                .stream()
                .filter(person -> !person.equals(me))
                .forEach(person -> {
                    notification.setReceiver(person.getEmail());
                    messagingTemplate.convertAndSendToUser(person.getEmail(), "/queue/notify", notification);
                    logger.info("Send notification to " + notification.getReceiver() + ": " + notification);
                });
    }
}
