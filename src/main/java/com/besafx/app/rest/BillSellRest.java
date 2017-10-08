package com.besafx.app.rest;

import com.besafx.app.entity.BillSell;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.TransactionSell;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.search.BillSellSearch;
import com.besafx.app.service.BillSellService;
import com.besafx.app.service.PersonService;
import com.besafx.app.service.TransactionSellService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
@RequestMapping(value = "/api/billSell/")
public class BillSellRest {

    private final Logger log = LoggerFactory.getLogger(BillSellRest.class);

    public static final String FILTER_TABLE = "**,order[id,code,treatedCount,unTreatedCount,falcon[**,customer[id,name]]],transactionSells[**,-billSell,drugUnit[**,-drugUnit],transactionBuy[**,drugUnit[**,-drugUnit],drug[**,-drugCategory,-transactionBuys],-billBuy,-transactionSells]]";
    public static final String FILTER_BILL_SELL_COMBO = "id,code";

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private TransactionSellService transactionSellService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BillSellSearch billSellSearch;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_CREATE')")
    @Transactional
    public String create(@RequestBody BillSell billSell, Principal principal) {
        BillSell billSellByOrder = billSellService.findByOrderAndOrderNotNull(billSell.getOrder());
        if(billSellByOrder == null){
            BillSell topBillSell = billSellService.findTopByOrderByCodeDesc();
            if (topBillSell == null) {
                billSell.setCode(1);
            } else {
                billSell.setCode(topBillSell.getCode() + 1);
            }
            billSell.setDate(new DateTime().toDate());
            billSell = billSellService.save(billSell);
        }
        ListIterator<TransactionSell> listIterator = billSell.getTransactionSells().listIterator();
        while (listIterator.hasNext()) {
            TransactionSell transactionSell = listIterator.next();
            if(billSellByOrder == null){
                transactionSell.setBillSell(billSell);
            }else{
                transactionSell.setBillSell(billSellByOrder);
            }
            TransactionSell topTransactionSell = transactionSellService.findTopByOrderByCodeDesc();
            if (topTransactionSell == null) {
                transactionSell.setCode(1);
            } else {
                transactionSell.setCode(topTransactionSell.getCode() + 1);
            }
            transactionSell.setDate(new DateTime().toDate());
            listIterator.set(transactionSellService.save(transactionSell));
        }
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "تم انشاء فاتورة بيع بنجاح" : "Create Bill Sell Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSell);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        BillSell billSell = billSellService.findOne(id);
        if (billSell != null) {
            transactionSellService.delete(billSell.getTransactionSells());
            billSellService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                    .message(lang.equals("AR") ? "تم حذف فاتورة بيع بنجاح" : "Delete Bill Sell Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "pay/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_PAY')")
    @Transactional
    public String pay(@PathVariable Long id) {
        BillSell billSell = billSellService.findOne(id);
        billSell.setPaymentMethod(PaymentMethod.Cash);
        billSell = billSellService.save(billSell);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSell);
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<BillSell> list = Lists.newArrayList(billSellService.findAll());
        list.sort(Comparator.comparing(BillSell::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<BillSell> list = Lists.newArrayList(billSellService.findAll());
        list.sort(Comparator.comparing(BillSell::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_BILL_SELL_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "viewInsideSalesTable", required = false) final Boolean viewInsideSalesTable,
            @RequestParam(value = "paymentMethods", required = false) final List<PaymentMethod> paymentMethods,
            @RequestParam(value = "checkCode", required = false) final String checkCode,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "orderCodeFrom", required = false) final Long orderCodeFrom,
            @RequestParam(value = "orderCodeTo", required = false) final Long orderCodeTo,
            @RequestParam(value = "orderFalconCode", required = false) final String orderFalconCode,
            @RequestParam(value = "orderCustomerName", required = false) final String orderCustomerName,
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
        List<BillSell> list = billSellSearch.filter(codeFrom, codeTo, viewInsideSalesTable, paymentMethods, checkCode, dateFrom, dateTo, orderCodeFrom, orderCodeTo, orderFalconCode, orderCustomerName);
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
