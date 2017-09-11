package com.besafx.app.rest;

import com.besafx.app.entity.BillSellDetection;
import com.besafx.app.entity.TransactionSellDetection;
import com.besafx.app.service.BillSellDetectionService;
import com.besafx.app.service.TransactionSellDetectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/transactionSellDetection/")
public class TransactionSellDetectionRest {

    private final Logger log = LoggerFactory.getLogger(TransactionSellDetectionRest.class);

    public static final String FILTER_TABLE = "**,detectionType[id],billSellDetection[id]";

    @Autowired
    private TransactionSellDetectionService transactionSellDetectionService;

    @Autowired
    private BillSellDetectionService billSellDetectionService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DETECTION_CREATE')")
    @Transactional
    public String create(@RequestBody TransactionSellDetection transactionSellDetection) {
        transactionSellDetection = transactionSellDetectionService.save(transactionSellDetection);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionSellDetection);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DETECTION_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id) {
        TransactionSellDetection transactionSellDetection = transactionSellDetectionService.findOne(id);
        if (transactionSellDetection != null) {
            transactionSellDetectionService.delete(id);
        }
    }

    @RequestMapping(value = "deleteByBillSellDetection/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DETECTION_DELETE')")
    @Transactional
    public void deleteByBillSellDetection(@PathVariable Long id) {
        BillSellDetection billSellDetection = billSellDetectionService.findOne(id);
        if (billSellDetection != null) {
            transactionSellDetectionService.delete(billSellDetection.getTransactionSellDetections());
        }
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), transactionSellDetectionService.findOne(id));
    }

    @RequestMapping(value = "findByBillSellDetection/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBillSellDetection(@PathVariable Long id) {
        BillSellDetection billSellDetection = billSellDetectionService.findOne(id);
        if (billSellDetection != null) {
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellDetection.getTransactionSellDetections());
        }else{
            return null;
        }
    }
}
