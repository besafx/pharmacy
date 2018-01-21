package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.OrderReceipt;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.search.OrderReceiptSearch;
import com.besafx.app.service.FundService;
import com.besafx.app.service.OrderReceiptService;
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
@RequestMapping(value = "/api/orderReceipt/")
public class OrderReceiptRest {

    public static final String FILTER_TABLE = "**,-fund,order[id,code],receipt[**,lastPerson[id,nickname,name]]";
    private final static Logger log = LoggerFactory.getLogger(OrderReceiptRest.class);
    @Autowired
    private OrderReceiptService orderReceiptService;

    @Autowired
    private OrderReceiptSearch orderReceiptSearch;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private FundService fundService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_RECEIPT_CREATE')")
    public String create(@RequestBody OrderReceipt orderReceipt, Principal principal) {
        if (orderReceipt.getReceipt().getAmountNumber() == 0) {
            throw new CustomException("لا يمكن إنشاء سند بقيمة صفر");
        }
        Person caller = personService.findByEmail(principal.getName());
        Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
        if (topReceipt == null) {
            orderReceipt.getReceipt().setCode(new Long(1));
        } else {
            orderReceipt.getReceipt().setCode(topReceipt.getCode() + 1);
        }
        orderReceipt.getReceipt().setAmountString(ArabicLiteralNumberParser.literalValueOf(orderReceipt.getReceipt().getAmountNumber()));
        orderReceipt.getReceipt().setReceiptType(ReceiptType.In);
        orderReceipt.getReceipt().setDate(new DateTime().toDate());
        orderReceipt.getReceipt().setLastUpdate(new DateTime().toDate());
        orderReceipt.getReceipt().setLastPerson(caller);
        orderReceipt.setReceipt(receiptService.save(orderReceipt.getReceipt()));
        orderReceipt.setFund(fundService.findFirstBy());
        orderReceipt = orderReceiptService.save(orderReceipt);
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .message(lang.equals("AR") ? "تم انشاء السند بنجاح" : "Create Receipt Successfully")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderReceipt);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_RECEIPT_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        OrderReceipt orderReceipt = orderReceiptService.findOne(id);
        if (orderReceipt != null) {
            orderReceiptService.delete(orderReceipt);
            receiptService.delete(orderReceipt.getReceipt());
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification.builder().message(lang.equals("AR") ? "تم حذف السند وكل ما يتعلق به من حسابات بنجاح" : "Delete Receipt With All Related Successfully").build(), principal.getName());
        }
    }

    @RequestMapping(value = "findByOrder/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByOrder(@PathVariable Long id) {
        List<OrderReceipt> list = Lists.newArrayList(orderReceiptService.findByOrderId(id));
        list.sort(Comparator.comparing(orderReceipt -> orderReceipt.getReceipt().getCode()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderReceiptService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "orderCodeFrom", required = false) final Long orderCodeFrom,
            @RequestParam(value = "orderCodeTo", required = false) final Long orderCodeTo,
            @RequestParam(value = "orderCustomerName", required = false) final String orderCustomerName,
            @RequestParam(value = "orderCustomerMobile", required = false) final String orderCustomerMobile,
            @RequestParam(value = "orderFalconCode", required = false) final Long orderFalconCode,
            @RequestParam(value = "orderFalconType", required = false) final String orderFalconType,
            @RequestParam(value = "orderDateFrom", required = false) final Long orderDateFrom,
            @RequestParam(value = "orderDateTo", required = false) final Long orderDateTo,

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
        List<OrderReceipt> list = orderReceiptSearch.filter(
                orderCodeFrom,
                orderCodeTo,
                orderCustomerName,
                orderCustomerMobile,
                orderFalconCode,
                orderFalconType,
                orderDateFrom,
                orderDateTo,
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
}
