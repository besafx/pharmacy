package com.besafx.app.rest;

import com.besafx.app.entity.*;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.search.BillBuySearch;
import com.besafx.app.service.*;
import com.besafx.app.util.ArabicLiteralNumberParser;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/billBuy/")
public class BillBuyRest {

    private final Logger log = LoggerFactory.getLogger(BillBuyRest.class);

    public static final String FILTER_TABLE = "**,supplier[id,code,name],transactionBuys[id],-billBuyReceipts";
    public static final String FILTER_TABLE_DETAILS = "**,supplier[id,code,name],transactionBuys[**,drugUnit[id,code,name],drug[id,code,nameArabic,nameEnglish],-billBuy,-transactionSells],billBuyReceipts[**,-billBuy,receipt[**,lastPerson[id,name]]]";
    public static final String FILTER_BILL_BUY_COMBO = "id,code";

    @Autowired
    private BillBuyService billBuyService;

    @Autowired
    private TransactionBuyService transactionBuyService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private BillBuyReceiptService billBuyReceiptService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BillBuySearch billBuySearch;

    @Autowired
    private TransactionSellService transactionSellService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_CREATE')")
    @Transactional
    public String create(@RequestBody BillBuy billBuy, Principal principal) {
        Person caller = personService.findByEmail(principal.getName());
        BillBuy topBillBuy = billBuyService.findTopByOrderByCodeDesc();
        if (topBillBuy == null) {
            billBuy.setCode(1);
        } else {
            billBuy.setCode(topBillBuy.getCode() + 1);
        }
        billBuy.setDate(new DateTime().toDate());
        billBuy = billBuyService.save(billBuy);
        {
            ListIterator<TransactionBuy> listIterator = billBuy.getTransactionBuys().listIterator();
            while (listIterator.hasNext()) {
                TransactionBuy transactionBuy = listIterator.next();
                TransactionBuy topTransactionBuy = transactionBuyService.findTopByOrderByCodeDesc();
                if (topTransactionBuy == null) {
                    transactionBuy.setCode(1);
                } else {
                    transactionBuy.setCode(topTransactionBuy.getCode() + 1);
                }
                transactionBuy.setBillBuy(billBuy);
                transactionBuy.setDate(new DateTime().toDate());
                listIterator.set(transactionBuyService.save(transactionBuy));
            }
        }
        {
            ListIterator<BillBuyReceipt> listIterator = billBuy.getBillBuyReceipts().listIterator();
            while (listIterator.hasNext()) {
                BillBuyReceipt billBuyReceipt = listIterator.next();
                if (billBuyReceipt.getReceipt().getAmountNumber() == 0) {
                    log.info("تجاهل إنشاء السند لقيمته الصفرية");
                    break;
                }
                //
                Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
                if (topReceipt == null) {
                    billBuyReceipt.getReceipt().setCode(new Long(1));
                } else {
                    billBuyReceipt.getReceipt().setCode(topReceipt.getCode() + 1);
                }
                billBuyReceipt.getReceipt().setAmountString(ArabicLiteralNumberParser.literalValueOf(billBuyReceipt.getReceipt().getAmountNumber()));
                billBuyReceipt.getReceipt().setReceiptType(ReceiptType.In);
                billBuyReceipt.getReceipt().setDate(new DateTime().toDate());
                billBuyReceipt.getReceipt().setNote("رسوم فاتورة الشراء رقم: " + billBuy.getCode());
                billBuyReceipt.getReceipt().setLastUpdate(new DateTime().toDate());
                billBuyReceipt.getReceipt().setLastPerson(caller);
                billBuyReceipt.setReceipt(receiptService.save(billBuyReceipt.getReceipt()));
                //
                billBuyReceipt.setBillBuy(billBuy);
                listIterator.set(billBuyReceiptService.save(billBuyReceipt));
            }
        }
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
            transactionSellService.delete(billBuy.findTransactionSells());
            transactionBuyService.delete(billBuy.getTransactionBuys());
            billBuyReceiptService.delete(billBuy.getBillBuyReceipts());
            receiptService.delete(billBuy.findReceipts());
            billBuyService.delete(billBuy);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف فاتورة شراء وكل السندات المتصلة بها بنجاح" : "Delete Bill Purchase And All Related Successfully")
                    .type("error")
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

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<BillBuy> list = Lists.newArrayList(billBuyService.findAll());
        list.sort(Comparator.comparing(BillBuy::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_BILL_BUY_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE_DETAILS), billBuyService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "suppliers", required = false) final List<Long> suppliers) {
        List<BillBuy> list = billBuySearch.filter(codeFrom, codeTo, dateFrom, dateTo, suppliers);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findByToday", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByToday() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuySearch.findByToday());
    }

    @RequestMapping(value = "findByWeek", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByWeek() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuySearch.findByWeek());
    }

    @RequestMapping(value = "findByMonth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByMonth() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuySearch.findByMonth());
    }

    @RequestMapping(value = "findByYear", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByYear() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuySearch.findByYear());
    }
}
