package com.besafx.app.rest;

import com.besafx.app.entity.Fund;
import com.besafx.app.entity.Person;
import com.besafx.app.service.FundService;
import com.besafx.app.service.PersonService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/fund/")
public class FundRest {

    public static final String FILTER_TABLE = "**,-orderReceipts,-billSellReceipts,-fundReceipts,lastPerson[id,nickname,name]";
    private final static Logger log = LoggerFactory.getLogger(FundRest.class);
    @Autowired
    private FundService fundService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_FUND_UPDATE')")
    public String update(@RequestBody Fund fund, Principal principal) {
        Fund object = fundService.findOne(fund.getId());
        if (object != null) {
            fund = fundService.save(fund);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification.builder().message(lang.equals("AR") ? "تم تعديل بيانات الصندوق بنجاح" : "Update Fund Information Successfully").type("warning").build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), fund);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String get() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), fundService.findFirstBy());
    }

}
