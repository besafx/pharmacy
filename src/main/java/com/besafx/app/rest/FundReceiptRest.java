package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.FundReceipt;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.search.FundReceiptSearch;
import com.besafx.app.service.FundReceiptService;
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
@RequestMapping(value = "/api/fundReceipt/")
public class FundReceiptRest {

    private final static Logger log = LoggerFactory.getLogger(FundReceiptRest.class);

    public static final String FILTER_TABLE = "**,fund[id,code],receipt[**,lastPerson[id,nickname,name]]";

    @Autowired
    private FundReceiptService fundReceiptService;

    @Autowired
    private FundReceiptSearch fundReceiptSearch;

    @Autowired
    private FundService fundService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    private FundReceipt create(ReceiptType receiptType, FundReceipt fundReceipt, String byEmail) {
        Person caller = personService.findByEmail(byEmail);
        Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
        if (topReceipt == null) {
            fundReceipt.getReceipt().setCode(new Long(1));
        } else {
            fundReceipt.getReceipt().setCode(topReceipt.getCode() + 1);
        }
        fundReceipt.getReceipt().setAmountString(ArabicLiteralNumberParser.literalValueOf(fundReceipt.getReceipt().getAmountNumber()));
        fundReceipt.getReceipt().setReceiptType(receiptType);
        fundReceipt.getReceipt().setDate(new DateTime().toDate());
        fundReceipt.getReceipt().setLastUpdate(new DateTime().toDate());
        fundReceipt.getReceipt().setLastPerson(caller);
        fundReceipt.setReceipt(receiptService.save(fundReceipt.getReceipt()));
        fundReceipt = fundReceiptService.save(fundReceipt);
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification.builder().message(lang.equals("AR") ? "تم انشاء السند للصندوق بنجاح" : "Create Cash Receipt Successfully").type("success").build(), byEmail);
        return fundReceipt;
    }

    @RequestMapping(value = "createIn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_FUND_RECEIPT_IN_CREATE')")
    public String createIn(@RequestBody FundReceipt fundReceipt, Principal principal) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), create(ReceiptType.In, fundReceipt, principal.getName()));
    }

    @RequestMapping(value = "createOut", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_FUND_RECEIPT_OUT_CREATE')")
    public String createOut(@RequestBody FundReceipt fundReceipt, Principal principal) {
        Double fundBalance = fundService.findOne(fundReceipt.getFund().getId()).getBalance();
        if(fundReceipt.getReceipt().getAmountNumber() > fundBalance){
            throw new CustomException("لا يمكن صرف قيمة أكبر من رصيد الصندوق = ".concat(fundBalance.toString()));
        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), create(ReceiptType.Out, fundReceipt, principal.getName()));
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_FUND_RECEIPT_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        FundReceipt fundReceipt = fundReceiptService.findOne(id);
        if (fundReceipt != null) {
            fundReceiptService.delete(fundReceipt);
            receiptService.delete(fundReceipt.getReceipt());
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification.builder().message(lang.equals("AR") ? "تم حذف السند وكل ما يتعلق به من حسابات الصندوق بنجاح" : "Delete Receipt With All Related Successfully").build(), principal.getName());
        }
    }

    @RequestMapping(value = "findByFund/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByFund(@PathVariable Long id) {
        List<FundReceipt> list = Lists.newArrayList(fundReceiptService.findByFundId(id));
        list.sort(Comparator.comparing(fundReceipt -> fundReceipt.getReceipt().getCode()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), fundReceiptService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "fundCodeFrom", required = false) final Long fundCodeFrom,
            @RequestParam(value = "fundCodeTo", required = false) final Long fundCodeTo,

            @RequestParam(value = "receiptType", required = false) final ReceiptType receiptType,
            @RequestParam(value = "receiptCode", required = false) final Long receiptCode,
            @RequestParam(value = "receiptDateFrom", required = false) final Long receiptDateFrom,
            @RequestParam(value = "receiptDateTo", required = false) final Long receiptDateTo,
            @RequestParam(value = "receiptLastUpdateFrom", required = false) final Long receiptLastUpdateFrom,
            @RequestParam(value = "receiptLastUpdateTo", required = false) final Long receiptLastUpdateTo,
            @RequestParam(value = "receiptPaymentMethods", required = false) final List<PaymentMethod> receiptPaymentMethods,
            @RequestParam(value = "receiptCheckCode", required = false) final Long receiptCheckCode,
            @RequestParam(value = "receiptAmountFrom", required = false) final Double receiptAmountFrom,
            @RequestParam(value = "receiptAmountTo", required = false) final Double receiptAmountTo,
            @RequestParam(value = "receiptReceiptTypes", required = false) final List<ReceiptType> receiptReceiptTypes,
            @RequestParam(value = "receiptPersonIds", required = false) final List<Long> receiptPersonIds) {
        List<FundReceipt> list = fundReceiptSearch.filter(
                fundCodeFrom,
                fundCodeTo,
                receiptType,
                receiptCode,
                receiptDateFrom,
                receiptDateTo,
                receiptLastUpdateFrom,
                receiptLastUpdateTo,
                receiptPaymentMethods,
                receiptCheckCode,
                receiptAmountFrom,
                receiptAmountTo,
                receiptReceiptTypes,
                receiptPersonIds);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findByToday/{receiptType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByToday(@PathVariable(value = "receiptType") ReceiptType receiptType) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), fundReceiptSearch.findByToday(receiptType));
    }

    @RequestMapping(value = "findByWeek/{receiptType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByWeek(@PathVariable(value = "receiptType") ReceiptType receiptType) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), fundReceiptSearch.findByWeek(receiptType));
    }

    @RequestMapping(value = "findByMonth/{receiptType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByMonth(@PathVariable(value = "receiptType") ReceiptType receiptType) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), fundReceiptSearch.findByMonth(receiptType));
    }

    @RequestMapping(value = "findByYear/{receiptType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByYear(@PathVariable(value = "receiptType") ReceiptType receiptType) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), fundReceiptSearch.findByYear(receiptType));
    }
}
