package com.besafx.app.rest;

import com.besafx.app.entity.BillBuy;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.TransactionBuy;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.search.BillBuySearch;
import com.besafx.app.service.BillBuyService;
import com.besafx.app.service.PersonService;
import com.besafx.app.service.TransactionBuyService;
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
import java.util.ListIterator;

@RestController
@RequestMapping(value = "/api/billBuy/")
public class BillBuyRest {

    private final Logger log = LoggerFactory.getLogger(BillBuyRest.class);

    public static final String FILTER_TABLE = "**,transactionBuys[**,drugUnit[**],drug[**,-drugCategory,-transactionBuys],-billBuy]";

    @Autowired
    private BillBuyService billBuyService;

    @Autowired
    private TransactionBuyService transactionBuyService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BillBuySearch billBuySearch;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_CREATE')")
    @Transactional
    public String create(@RequestBody BillBuy billBuy, Principal principal) {
        BillBuy topBillBuy = billBuyService.findTopByOrderByCodeDesc();
        if (topBillBuy == null) {
            billBuy.setCode(1);
        } else {
            billBuy.setCode(topBillBuy.getCode() + 1);
        }
        billBuy.setDate(new DateTime().toDate());
        billBuy = billBuyService.save(billBuy);
        ListIterator<TransactionBuy> listIterator = billBuy.getTransactionBuys().listIterator();
        while (listIterator.hasNext()) {
            TransactionBuy transactionBuy = listIterator.next();
            transactionBuy.setBillBuy(billBuy);
            TransactionBuy topTransactionBuy = transactionBuyService.findTopByOrderByCodeDesc();
            if (topTransactionBuy == null) {
                transactionBuy.setCode(1);
            } else {
                transactionBuy.setCode(topTransactionBuy.getCode() + 1);
            }
            transactionBuy.setDate(new DateTime().toDate());
            listIterator.set(transactionBuyService.save(transactionBuy));
        }
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "تم انشاء فاتورة شراء بنجاح" : "Create Bill Buy Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuy);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        BillBuy billBuy = billBuyService.findOne(id);
        if (billBuy != null) {
            transactionBuyService.delete(billBuy.getTransactionBuys());
            billBuyService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                    .message(lang.equals("AR") ? "تم حذف فاتورة شراء بنجاح" : "Delete Bill Buy Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<BillBuy> list = Lists.newArrayList(billBuyService.findAll());
        list.sort(Comparator.comparing(BillBuy::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuyService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "paymentMethods", required = false) final List<PaymentMethod> paymentMethods,
            @RequestParam(value = "checkCode", required = false) final String checkCode,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "suppliers", required = false) final List<Long> suppliers,
            Principal principal) {
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "جاري تصفية النتائج، فضلاً انتظر قليلا..." : "Filtering Data")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        List<BillBuy> list = billBuySearch.filter(codeFrom, codeTo, paymentMethods, checkCode, dateFrom, dateTo, suppliers);
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "تمت العملية بنجاح" : "job Done")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }
}
