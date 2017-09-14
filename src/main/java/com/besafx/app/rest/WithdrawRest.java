package com.besafx.app.rest;

import com.besafx.app.entity.Deposit;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Withdraw;
import com.besafx.app.service.PersonService;
import com.besafx.app.service.WithdrawService;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/withdraw/")
public class WithdrawRest {

    private final static Logger log = LoggerFactory.getLogger(WithdrawRest.class);

    public static final String FILTER_TABLE = "**,bank[id]";

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_WITHDRAW_CREATE')")
    @Transactional
    @Async("threadPoolBank")
    public String create(@RequestBody Withdraw withdraw, Principal principal) {
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        double totalDeposits = withdraw.getBank().getDeposits().stream().mapToDouble(Deposit::getAmount).sum();
        log.info("إجمالي الإيداعات = " + totalDeposits);
        double totalWithdraws = withdraw.getBank().getWithdraws().stream().mapToDouble(Withdraw::getAmount).sum();
        log.info("إجمالي السحبيات = " + totalWithdraws);
        double realBalance = totalDeposits - totalWithdraws;
        log.info("الرصيد الحالي = " + realBalance);
        if (withdraw.getAmount() > realBalance) {
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "السحبيات البنكية" : "Data Processing")
                    .message(lang.equals("AR") ? "لا يمكن تجاوز الحد الأقصي لرصيد الحساب البنكي" : "Maximum Bank Account Balance Cannot Be Exceeded")
                    .type("error")
                    .icon("fa-ban")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return null;
        }
        withdraw = withdrawService.save(withdraw);
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "السحبيات البنكية" : "Data Processing")
                .message(lang.equals("AR") ? "تم حفظ السحب بنجاح" : "Create Withdraw Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), withdraw);
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Withdraw> list = Lists.newArrayList(withdrawService.findAll());
        list.sort(Comparator.comparing(Withdraw::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), withdrawService.findOne(id));
    }

    @RequestMapping(value = "findByBank/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBank(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), withdrawService.findByBankId(id));
    }
}
