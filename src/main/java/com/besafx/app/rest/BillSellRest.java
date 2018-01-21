package com.besafx.app.rest;

import com.besafx.app.entity.*;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.search.BillSellSearch;
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
import com.sun.org.apache.xpath.internal.functions.FuncDoclocation;
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

    public static final String FILTER_TABLE = "**,billSellReceipts[id,receipt[**,lastPerson[id,nickname,name]]],lastPerson[id,name],order[id,code,treatedCount,unTreatedCount,falcon[id,code,type,weight,-orders,customer[id,name]]],transactionSells[**,-billSell,drugUnit[**,-drugUnit],transactionBuy[**,drugUnit[**,-drugUnit],drug[**,-drugCategory,-transactionSells,-transactionBuys],-billBuy,-transactionSells]]";
    public static final String FILTER_Inside_Debt = "id,code,date,discount,order[id,code,date,falcon[id,code,customer[id,name]]],unitSellCostSum,net,paid,remain,-lastPerson,-transactionSells,-billSellReceipts";
    public static final String FILTER_Outside_Debt = "id,code,date,discount,customerName,falconCode,unitSellCostSum,net,paid,remain,-lastPerson,-transactionSells,-billSellReceipts";
    public static final String FILTER_BILL_SELL_COMBO = "id,code";
    public static final String FILTER_BILL_SELL_PRICES = "net,paid,remain";
    private final Logger log = LoggerFactory.getLogger(BillSellRest.class);
    @Autowired
    private BillSellService billSellService;

    @Autowired
    private TransactionSellService transactionSellService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private FundService fundService;

    @Autowired
    private BillSellReceiptService billSellReceiptService;

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
        Person caller = personService.findByEmail(principal.getName());
        BillSell billSellByOrder = billSellService.findByOrderAndOrderNotNull(billSell.getOrder());
        if (billSellByOrder == null) {
            BillSell topBillSell = billSellService.findTopByOrderByCodeDesc();
            if (topBillSell == null) {
                billSell.setCode(1);
            } else {
                billSell.setCode(topBillSell.getCode() + 1);
            }
            billSell.setDate(new DateTime().toDate());
            billSell = billSellService.save(billSell);
        }
        {
            ListIterator<TransactionSell> listIterator = billSell.getTransactionSells().listIterator();
            while (listIterator.hasNext()) {
                TransactionSell transactionSell = listIterator.next();
                if (billSellByOrder == null) {
                    transactionSell.setBillSell(billSell);
                } else {
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
        }
        {
            ListIterator<BillSellReceipt> listIterator = billSell.getBillSellReceipts().listIterator();
            while (listIterator.hasNext()) {
                BillSellReceipt billSellReceipt = listIterator.next();
                if (billSellReceipt.getReceipt().getAmountNumber() == 0) {
                    log.info("تجاهل إنشاء السند لقيمته الصفرية");
                    break;
                }
                //
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
                //
                billSellReceipt.setBillSell(billSell);
                billSellReceipt.setFund(fundService.findFirstBy());
                listIterator.set(billSellReceiptService.save(billSellReceipt));
            }
        }
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم انشاء فاتورة بيع بنجاح" : "Create Bill Sell Successfully")
                .type("success")
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
            billSellReceiptService.delete(billSell.getBillSellReceipts());
            receiptService.delete(billSell.findReceipts());
            billSellService.delete(billSell);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .message(lang.equals("AR") ? "تم حذف فاتورة بيع بنجاح" : "Delete Bill Sell Successfully")
                    .type("error")
                    .build(), principal.getName());
        }
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

    @RequestMapping(value = "findPrices/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findPrices(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_BILL_SELL_PRICES), billSellService.findOne(id));
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
        List<BillSell> list = billSellSearch.filterInside(codeFrom, codeTo, dateFrom, dateTo, orderCodeFrom, orderCodeTo, orderFalconCode, orderCustomerName);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Inside_Debt), list);
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
        List<BillSell> list = billSellSearch.filterOutside(codeFrom, codeTo, dateFrom, dateTo, orderFalconCode, orderCustomerName);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Outside_Debt), list);
    }

    @RequestMapping(value = "findInsideSalesByToday", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findInsideSalesByToday() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Inside_Debt), billSellSearch.findInsideSalesByToday());
    }

    @RequestMapping(value = "findInsideSalesByWeek", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findInsideSalesByWeek() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Inside_Debt), billSellSearch.findInsideSalesByWeek());
    }

    @RequestMapping(value = "findInsideSalesByMonth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findInsideSalesByMonth() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Inside_Debt), billSellSearch.findInsideSalesByMonth());
    }

    @RequestMapping(value = "findInsideSalesByYear", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findInsideSalesByYear() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Inside_Debt), billSellSearch.findInsideSalesByYear());
    }

    @RequestMapping(value = "findOutsideSalesByToday", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOutsideSalesByToday() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Outside_Debt), billSellSearch.findOutsideSalesByToday());
    }

    @RequestMapping(value = "findOutsideSalesByWeek", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOutsideSalesByWeek() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Outside_Debt), billSellSearch.findOutsideSalesByWeek());
    }

    @RequestMapping(value = "findOutsideSalesByMonth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOutsideSalesByMonth() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Outside_Debt), billSellSearch.findOutsideSalesByMonth());
    }

    @RequestMapping(value = "findOutsideSalesByYear", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOutsideSalesByYear() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_Outside_Debt), billSellSearch.findOutsideSalesByYear());
    }
}
