package com.besafx.app.rest;

import com.besafx.app.entity.BillSellDetection;
import com.besafx.app.entity.Person;
import com.besafx.app.service.BillSellDetectionService;
import com.besafx.app.service.PersonService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/billSellDetection/")
public class BillSellDetectionRest {

    private final Logger log = LoggerFactory.getLogger(BillSellDetectionRest.class);

    public static final String FILTER_TABLE = "**,order[id]";

    @Autowired
    private BillSellDetectionService billSellDetectionService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DETECTION_CREATE')")
    @Transactional
    public String create(@RequestBody BillSellDetection billSellDetection, Principal principal) {
        BillSellDetection topBillSellDetection = billSellDetectionService.findTopByOrderByCodeDesc();
        if (topBillSellDetection == null) {
            billSellDetection.setCode(1);
        } else {
            billSellDetection.setCode(topBillSellDetection.getCode() + 1);
        }
        billSellDetection.setDate(new DateTime().toDate());
        billSellDetection = billSellDetectionService.save(billSellDetection);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "تم انشاء فاتورة بيع فحوصات بنجاح" : "Create Bill Sell For Detections Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellDetection);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DETECTION_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        BillSellDetection billSellDetection = billSellDetectionService.findOne(id);
        if (billSellDetection != null) {
            billSellDetectionService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                    .message(lang.equals("AR") ? "تم حذف فاتورة بيع الفحوصات بنجاح" : "Delete Bill Sell For Detections Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<BillSellDetection> list = Lists.newArrayList(billSellDetectionService.findAll());
        list.sort(Comparator.comparing(BillSellDetection::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellDetectionService.findOne(id));
    }
}
