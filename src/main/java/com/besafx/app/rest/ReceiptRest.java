package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.Person;
import com.besafx.app.service.ReceiptService;
import com.besafx.app.service.FalconService;
import com.besafx.app.service.OrderService;
import com.besafx.app.service.PersonService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/receipt/")
public class ReceiptRest {

    private final static Logger log = LoggerFactory.getLogger(ReceiptRest.class);

    public static final String FILTER_TABLE = "**,lastPerson[id,nickname,name]";

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_RECEIPT_CREATE')")
    public String create(@RequestBody Receipt receipt, Principal principal) {
        Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
        if (topReceipt == null) {
            receipt.setCode(Long.valueOf(1));
        } else {
            receipt.setCode(topReceipt.getCode() + 1);
        }
        Person caller = personService.findByEmail(principal.getName());
        receipt.setDate(new Date());
        receipt.setLastUpdate(new Date());
        receipt.setLastPerson(caller);
        receipt = receiptService.save(receipt);
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "السندات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء السند بنجاح" : "Create Receipt Account Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), receipt);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_RECEIPT_UPDATE')")
    public String update(@RequestBody Receipt receipt, Principal principal) {
        if (receiptService.findByCodeAndIdIsNot(receipt.getCode(), receipt.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Receipt object = receiptService.findOne(receipt.getId());
        if (object != null) {
            receipt = receiptService.save(receipt);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "السندات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات السند بنجاح" : "Update Receipt Account Information Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), receipt);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_RECEIPT_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        Receipt receipt = receiptService.findOne(id);
        if (receipt != null) {
            receiptService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "السندات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف السند وكل ما يتعلق به من حسابات بنجاح" : "Delete Receipt Account With All Related Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Receipt> list = Lists.newArrayList(receiptService.findAll());
        list.sort(Comparator.comparing(Receipt::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), receiptService.findOne(id));
    }
}
