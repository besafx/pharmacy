package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.BankReceipt;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.search.BankReceiptSearch;
import com.besafx.app.service.BankReceiptService;
import com.besafx.app.service.BankService;
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
@RequestMapping(value = "/api/bankReceipt/")
public class BankReceiptRest {

    public static final String FILTER_TABLE = "**,bank[id,code],receipt[**,lastPerson[id,nickname,name]]";
    private final static Logger log = LoggerFactory.getLogger(BankReceiptRest.class);
    @Autowired
    private BankReceiptService bankReceiptService;

    @Autowired
    private BankReceiptSearch bankReceiptSearch;

    @Autowired
    private BankService bankService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    private BankReceipt create(ReceiptType receiptType, BankReceipt bankReceipt, String byEmail) {
        Person caller = personService.findByEmail(byEmail);
        Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
        if (topReceipt == null) {
            bankReceipt.getReceipt().setCode(new Long(1));
        } else {
            bankReceipt.getReceipt().setCode(topReceipt.getCode() + 1);
        }
        bankReceipt.getReceipt().setAmountString(ArabicLiteralNumberParser.literalValueOf(bankReceipt.getReceipt().getAmountNumber()));
        bankReceipt.getReceipt().setReceiptType(receiptType);
        bankReceipt.getReceipt().setDate(new DateTime().toDate());
        bankReceipt.getReceipt().setLastUpdate(new DateTime().toDate());
        bankReceipt.getReceipt().setLastPerson(caller);
        bankReceipt.setReceipt(receiptService.save(bankReceipt.getReceipt()));
        bankReceipt = bankReceiptService.save(bankReceipt);
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم انشاء السند للبنك بنجاح" : "Create Receipt Successfully")
                .type("warning").build(), byEmail);
        return bankReceipt;
    }

    @RequestMapping(value = "createIn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BANK_RECEIPT_IN_CREATE')")
    public String createIn(@RequestBody BankReceipt bankReceipt, Principal principal) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), create(ReceiptType.In, bankReceipt, principal.getName()));
    }

    @RequestMapping(value = "createOut", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BANK_RECEIPT_OUT_CREATE')")
    public String createOut(@RequestBody BankReceipt bankReceipt, Principal principal) {
        Double bankBalance = bankService.findOne(bankReceipt.getBank().getId()).getBalance();
        if (bankReceipt.getReceipt().getAmountNumber() > bankBalance) {
            throw new CustomException("لا يمكن صرف قيمة أكبر من رصيد البنك = ".concat(bankBalance.toString()));
        }
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), create(ReceiptType.Out, bankReceipt, principal.getName()));
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_BANK_RECEIPT_IN_DELETE') or hasRole('ROLE_BANK_RECEIPT_OUT_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        BankReceipt bankReceipt = bankReceiptService.findOne(id);
        if (bankReceipt != null) {
            bankReceiptService.delete(bankReceipt);
            receiptService.delete(bankReceipt.getReceipt());
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف السند وكل ما يتعلق به من حسابات بنجاح" : "Delete Receipt With All Related Successfully")
                    .type("error").build(), principal.getName());
        }
    }

    @RequestMapping(value = "findByBank/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByBank(@PathVariable Long id) {
        List<BankReceipt> list = Lists.newArrayList(bankReceiptService.findByBankId(id));
        list.sort(Comparator.comparing(bankReceipt -> bankReceipt.getReceipt().getCode()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), bankReceiptService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "bankCodeFrom", required = false) final Long bankCodeFrom,
            @RequestParam(value = "bankCodeTo", required = false) final Long bankCodeTo,
            @RequestParam(value = "bankName", required = false) final String bankName,
            @RequestParam(value = "bankBranchName", required = false) final String bankBranchName,

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
        List<BankReceipt> list = bankReceiptSearch.filter(
                bankCodeFrom,
                bankCodeTo,
                bankName,
                bankBranchName,
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
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), bankReceiptSearch.findByToday(receiptType));
    }

    @RequestMapping(value = "findByWeek/{receiptType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByWeek(@PathVariable(value = "receiptType") ReceiptType receiptType) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), bankReceiptSearch.findByWeek(receiptType));
    }

    @RequestMapping(value = "findByMonth/{receiptType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByMonth(@PathVariable(value = "receiptType") ReceiptType receiptType) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), bankReceiptSearch.findByMonth(receiptType));
    }

    @RequestMapping(value = "findByYear/{receiptType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByYear(@PathVariable(value = "receiptType") ReceiptType receiptType) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), bankReceiptSearch.findByYear(receiptType));
    }
}
