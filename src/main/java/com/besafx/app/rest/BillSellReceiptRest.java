package com.besafx.app.rest;

import com.besafx.app.auditing.Action;
import com.besafx.app.entity.BillSellReceipt;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.entity.listener.BillSellReceiptListener;
import com.besafx.app.search.BillSellReceiptSearch;
import com.besafx.app.service.BillSellReceiptService;
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
@RequestMapping(value = "/api/billSellReceipt/")
public class BillSellReceiptRest {

    public static final String FILTER_TABLE = "**,-fund,billSell[id,code],receipt[**,lastPerson[id,nickname,name]]";

    private final static Logger log = LoggerFactory.getLogger(BillSellReceiptRest.class);

    @Autowired
    private BillSellReceiptService billSellReceiptService;

    @Autowired
    private BillSellReceiptSearch billSellReceiptSearch;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private FundService fundService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BillSellReceiptListener billSellReceiptListener;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_CREATE')")
    public String create(@RequestBody BillSellReceipt billSellReceipt, Principal principal) {
        Person caller = personService.findByEmail(principal.getName());
        Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
        if (topReceipt == null) {
            billSellReceipt.getReceipt().setCode(new Long(1));
        } else {
            billSellReceipt.getReceipt().setCode(topReceipt.getCode() + 1);
        }
        billSellReceipt.getReceipt().setAmountString(ArabicLiteralNumberParser.literalValueOf(billSellReceipt.getReceipt().getAmountNumber()));
        billSellReceipt.getReceipt().setReceiptType(ReceiptType.In);
        billSellReceipt.getReceipt().setDate(new DateTime().toDate());
        billSellReceipt.getReceipt().setLastUpdate(new DateTime().toDate());
        billSellReceipt.getReceipt().setLastPerson(caller);
        billSellReceipt.setReceipt(receiptService.save(billSellReceipt.getReceipt()));
        billSellReceipt.setFund(fundService.findFirstBy());
        billSellReceipt = billSellReceiptService.save(billSellReceipt);
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم انشاء السند بنجاح" : "Create Receipt Successfully")
                .type("success")
                .build(), principal.getName());

        log.info("START CREATE HISTORY LINE");
        StringBuilder builder = new StringBuilder();
        builder.append("اضافة سند قبض رقم / ");
        builder.append(billSellReceipt.getReceipt().getCode());
        builder.append(" بقيمة ");
        builder.append(billSellReceipt.getReceipt().getAmountString());
        builder.append(" ريال سعودي ");
        builder.append(" إلى الفاتورة رقم / ");
        builder.append(billSellReceipt.getReceipt().getCode());
        billSellReceiptListener.perform(billSellReceipt, Action.INSERTED, builder.toString());
        log.info("END CREATE HISTORY LINE");

        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellReceipt);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BILL_SELL_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        BillSellReceipt billSellReceipt = billSellReceiptService.findOne(id);
        if (billSellReceipt != null) {
            billSellReceiptService.delete(billSellReceipt);
            receiptService.delete(billSellReceipt.getReceipt());
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف السند وكل ما يتعلق به من حسابات بنجاح" : "Delete Receipt With All Related Successfully")
                    .type("error")
                    .build(), principal.getName());

            log.info("START CREATE HISTORY LINE");
            StringBuilder builder = new StringBuilder();
            builder.append("حذف سند قبض رقم / ");
            builder.append(billSellReceipt.getReceipt().getCode());
            builder.append(" بقيمة ");
            builder.append(billSellReceipt.getReceipt().getAmountString());
            builder.append(" ريال سعودي ");
            builder.append(" من الفاتورة رقم / ");
            builder.append(billSellReceipt.getReceipt().getCode());
            billSellReceiptListener.perform(billSellReceipt, Action.DELETED, builder.toString());
            log.info("END CREATE HISTORY LINE");


        }
    }

    @RequestMapping(value = "findByBillSell/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBillSell(@PathVariable Long id) {
        List<BillSellReceipt> list = Lists.newArrayList(billSellReceiptService.findByBillSellId(id));
        list.sort(Comparator.comparing(billSellReceipt -> billSellReceipt.getReceipt().getCode()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), billSellReceiptService.findOne(id));
    }

    @RequestMapping(value = "filterInside", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filterInside(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "orderCodeFrom", required = false) final Long orderCodeFrom,
            @RequestParam(value = "orderCodeTo", required = false) final Long orderCodeTo,
            @RequestParam(value = "orderFalconCode", required = false) final String orderFalconCode,
            @RequestParam(value = "orderCustomerName", required = false) final String orderCustomerName) {
        List<BillSellReceipt> list = billSellReceiptSearch.filterInside(codeFrom, codeTo, dateFrom, dateTo, orderCodeFrom, orderCodeTo, orderFalconCode, orderCustomerName);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "filterOutside", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filterOutside(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "orderFalconCode", required = false) final String orderFalconCode,
            @RequestParam(value = "orderCustomerName", required = false) final String orderCustomerName) {
        List<BillSellReceipt> list = billSellReceiptSearch.filterOutside(codeFrom, codeTo, dateFrom, dateTo, orderFalconCode, orderCustomerName);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }
}
