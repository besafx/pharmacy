package com.besafx.app.rest;

import com.besafx.app.auditing.Action;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.*;
import com.besafx.app.entity.listener.TransactionSellListener;
import com.besafx.app.service.*;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/transactionSell/")
public class TransactionSellRest {

    public static final String FILTER_TABLE = "**,drugUnit[**,-drug,-drugUnit],transactionBuy[**,drugUnit[**,-drug,-drugUnit],drug[**,-defaultDrugUnit,-drugUnits,-drugCategory,-transactionSells,-transactionBuys],billBuy[id,code],-transactionSells],billSell[id,code]";

    private final Logger log = LoggerFactory.getLogger(TransactionSellRest.class);

    @Autowired
    private TransactionSellService transactionSellService;

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private TransactionBuyService transactionBuyService;

    @Autowired
    private DrugService drugService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionSellListener transactionSellListener;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_CREATE')")
    @Transactional
    public String create(@RequestBody TransactionSell transactionSell, Principal principal) {
        if (transactionSell.getBillSell().getOrder() != null) {
            throw new CustomException("لا يمكنك اضافة حركات بيع الى فاتورة صرف علاج طلب فحص");
        }
        TransactionSell topTransactionSell = transactionSellService.findTopByOrderByCodeDesc();
        if (topTransactionSell == null) {
            transactionSell.setCode(1);
        } else {
            transactionSell.setCode(topTransactionSell.getCode() + 1);
        }
        transactionSell.setDate(new DateTime().toDate());
        transactionSell = transactionSellService.save(transactionSell);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم اضافة الصنف بنجاح" : "Adding Item Successfully")
                .type("success")
                .build(), principal.getName());

        log.info("START CREATE HISTORY LINE");
        StringBuilder builder = new StringBuilder();
        builder.append("اضافة حركة بيع رقم / ");
        builder.append(transactionSell.getCode());
        builder.append(" بالصنف رقم / ");
        builder.append(transactionSell.getTransactionBuy().getDrug().getCode());
        builder.append(" إلى الفاتورة رقم / ");
        builder.append(transactionSell.getBillSell().getCode());
        transactionSellListener.perform(transactionSell, Action.INSERTED, builder.toString());
        log.info("END CREATE HISTORY LINE");

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionSell);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        TransactionSell transactionSell = transactionSellService.findOne(id);
        if (transactionSell != null) {
            transactionSellService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف الصنف بنجاح" : "Deleting Item Successfully")
                    .type("error")
                    .build(), principal.getName());

            log.info("START CREATE HISTORY LINE");
            StringBuilder builder = new StringBuilder();
            builder.append("حذف حركة بيع رقم / ");
            builder.append(transactionSell.getCode());
            builder.append(" بالصنف رقم / ");
            builder.append(transactionSell.getTransactionBuy().getDrug().getCode());
            builder.append(" من الفاتورة رقم / ");
            builder.append(transactionSell.getBillSell().getCode());
            transactionSellListener.perform(transactionSell, Action.DELETED, builder.toString());
            log.info("END CREATE HISTORY LINE");

        }
    }

    @RequestMapping(value = "deleteByBillSell/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DELETE')")
    @Transactional
    public void deleteByBillSell(@PathVariable Long id) {
        BillSell billSell = billSellService.findOne(id);
        if (billSell != null) {
            transactionSellService.delete(billSell.getTransactionSells());
        }
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionSellService.findOne(id));
    }

    @RequestMapping(value = "findByBillSell/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBillSell(@PathVariable Long id) {
        BillSell billSell = billSellService.findOne(id);
        if (billSell != null) {
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSell.getTransactionSells());
        } else {
            return null;
        }
    }

    @RequestMapping(value = "findByTransactionBuy/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByTransactionBuy(@PathVariable Long id) {
        TransactionBuy transactionBuy = transactionBuyService.findOne(id);
        if (transactionBuy != null) {
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionBuy.getTransactionSells());
        } else {
            return null;
        }
    }

    @RequestMapping(value = "findByDrug/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByDrug(@PathVariable Long id) {
        Drug drug = drugService.findOne(id);
        if (drug != null) {
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionSellService.findByTransactionBuyDrug(drug));
        } else {
            return null;
        }
    }
}
