package com.besafx.app.rest;

import com.besafx.app.entity.BillBuy;
import com.besafx.app.entity.TransactionBuy;
import com.besafx.app.service.BillBuyService;
import com.besafx.app.service.TransactionBuyService;
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

@RestController
@RequestMapping(value = "/api/transactionBuy/")
public class TransactionBuyRest {

    private final Logger log = LoggerFactory.getLogger(TransactionBuyRest.class);

    public static final String FILTER_TABLE = "**,drugUnit[**],drug[**,-drugCategory,-transactionBuys],-billBuy";

    @Autowired
    private TransactionBuyService transactionBuyService;

    @Autowired
    private BillBuyService billBuyService;

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
    public void delete(@PathVariable Long id) {
        TransactionBuy transactionBuy = transactionBuyService.findOne(id);
        if (transactionBuy != null) {
            transactionBuyService.delete(id);
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
}
