package com.besafx.app.rest;

import com.besafx.app.entity.BillBuyReceipt;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.search.BillBuyReceiptSearch;
import com.besafx.app.service.BillBuyReceiptService;
import com.besafx.app.service.FundService;
import com.besafx.app.service.PersonService;
import com.besafx.app.service.ReceiptService;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/billBuyReceipt/")
public class BillBuyReceiptRest {

    private final static Logger log = LoggerFactory.getLogger(BillBuyReceiptRest.class);

    public static final String FILTER_TABLE = "**,billBuy[id,code],receipt[**,lastPerson[id,nickname,name]]";

    @Autowired
    private BillBuyReceiptService billBuyReceiptService;

    @Autowired
    private BillBuyReceiptSearch billBuyReceiptSearch;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_RECEIPT_CREATE')")
    public String create(@RequestBody BillBuyReceipt billBuyReceipt, Principal principal) {
        Person caller = personService.findByEmail(principal.getName());
        Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
        if (topReceipt == null) {
            billBuyReceipt.getReceipt().setCode(new Long(1));
        } else {
            billBuyReceipt.getReceipt().setCode(topReceipt.getCode() + 1);
        }
        billBuyReceipt.getReceipt().setAmountString(ArabicLiteralNumberParser.literalValueOf(billBuyReceipt.getReceipt().getAmountNumber()));
        billBuyReceipt.getReceipt().setReceiptType(ReceiptType.In);
        billBuyReceipt.getReceipt().setDate(new DateTime().toDate());
        billBuyReceipt.getReceipt().setLastUpdate(new DateTime().toDate());
        billBuyReceipt.getReceipt().setLastPerson(caller);
        billBuyReceipt.setReceipt(receiptService.save(billBuyReceipt.getReceipt()));
        billBuyReceipt = billBuyReceiptService.save(billBuyReceipt);
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم انشاء السند بنجاح" : "Create Receipt Successfully")
                .type("success")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuyReceipt);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_BUY_RECEIPT_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        BillBuyReceipt billBuyReceipt = billBuyReceiptService.findOne(id);
        if (billBuyReceipt != null) {
            billBuyReceiptService.delete(billBuyReceipt);
            receiptService.delete(billBuyReceipt.getReceipt());
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف السند وكل ما يتعلق به من حسابات بنجاح" : "Delete Receipt With All Related Successfully")
                    .type("error")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findByBillBuy/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBillBuy(@PathVariable Long id) {
        List<BillBuyReceipt> list = Lists.newArrayList(billBuyReceiptService.findByBillBuyId(id));
        list.sort(Comparator.comparing(billBuyReceipt -> billBuyReceipt.getReceipt().getCode()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billBuyReceiptService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo) {
        List<BillBuyReceipt> list = billBuyReceiptSearch.filter(codeFrom, codeTo, dateFrom, dateTo);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }
}
