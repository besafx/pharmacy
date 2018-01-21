package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.BillBuy;
import com.besafx.app.entity.Drug;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.TransactionBuy;
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

@RestController
@RequestMapping(value = "/api/transactionBuy/")
public class TransactionBuyRest {

    public static final String FILTER_TABLE = "**,drugUnit[**,-drugUnit],drug[**,-drugCategory,-transactionSells,-transactionBuys],billBuy[id,code],-transactionSells";
    private final Logger log = LoggerFactory.getLogger(TransactionBuyRest.class);
    @Autowired
    private TransactionBuyService transactionBuyService;

    @Autowired
    private TransactionSellService transactionSellService;

    @Autowired
    private BillBuyService billBuyService;

    @Autowired
    private DrugService drugService;

    @Autowired
    private DrugUnitService drugUnitService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_CREATE')")
    @Transactional
    public String create(@RequestBody TransactionBuy transactionBuy) {
        TransactionBuy topTransactionBuy = transactionBuyService.findTopByOrderByCodeDesc();
        if (topTransactionBuy == null) {
            transactionBuy.setCode(1);
        } else {
            transactionBuy.setCode(topTransactionBuy.getCode() + 1);
        }
        transactionBuy.setDate(new DateTime().toDate());
        transactionBuy = transactionBuyService.save(transactionBuy);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionBuy);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        TransactionBuy transactionBuy = transactionBuyService.findOne(id);
        if (transactionBuy != null) {
            transactionSellService.delete(transactionBuy.getTransactionSells());
            transactionBuyService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "المشتريات" : "The Sales")
                    .message(lang.equals("AR") ? "تم حذف الطلبية وكل ما يتعلق بها من مبيعات بنجاح" : "Delete Purchace Order With All Related Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "deleteByBillBuy/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_DELETE')")
    @Transactional
    public void deleteByBillBuy(@PathVariable Long id) {
        BillBuy billBuy = billBuyService.findOne(id);
        if (billBuy != null) {
            transactionBuyService.delete(billBuy.getTransactionBuys());
        }
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionBuyService.findOne(id));
    }

    @RequestMapping(value = "findByBillBuy/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBillBuy(@PathVariable Long id) {
        BillBuy billBuy = billBuyService.findOne(id);
        if (billBuy != null) {
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuy.getTransactionBuys());
        } else {
            return null;
        }
    }

    @RequestMapping(value = "findByDrug/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByDrug(@PathVariable Long id) {
        Drug drug = drugService.findOne(id);
        if (drug != null) {
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drug.getTransactionBuys());
        } else {
            return null;
        }
    }

    @RequestMapping(value = "updatePrices/{transactionBuyId}/{drugUnitId}/{unitBuyCost}/{unitSellCost}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public String updatePrices(@PathVariable(value = "transactionBuyId") Long transactionBuyId,
                               @PathVariable(value = "drugUnitId") Long drugUnitId,
                               @PathVariable(value = "unitBuyCost") Double unitBuyCost,
                               @PathVariable(value = "unitSellCost") Double unitSellCost) {
        TransactionBuy transactionBuy = transactionBuyService.findOne(transactionBuyId);
        transactionBuy.setDrugUnit(drugUnitService.findOne(drugUnitId));
        transactionBuy.setUnitBuyCost(unitBuyCost);
        transactionBuy.setUnitSellCost(unitSellCost);
        transactionBuy = transactionBuyService.save(transactionBuy);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionBuy);
    }

    @RequestMapping(value = "updateQuantity/{transactionBuyId}/{quantity}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public String updateQuantity(@PathVariable(value = "transactionBuyId") Long transactionBuyId,
                                 @PathVariable(value = "quantity") Double quantity) {
        TransactionBuy transactionBuy = transactionBuyService.findOne(transactionBuyId);
        if (transactionBuy != null) {
            if (!transactionBuy.getTransactionSells().isEmpty()) {
                throw new CustomException("لا يمكنك التعديل فى الكميات بعد عمليات البيع");
            }
            transactionBuy.setQuantity(quantity);
            transactionBuy = transactionBuyService.save(transactionBuy);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionBuy);
        } else {
            return null;
        }
    }
}
