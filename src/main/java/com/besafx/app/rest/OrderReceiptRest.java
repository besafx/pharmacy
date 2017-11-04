package com.besafx.app.rest;

import com.besafx.app.entity.OrderReceipt;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.enums.ReceiptType;
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

    private final static Logger log = LoggerFactory.getLogger(OrderReceiptRest.class);

    public static final String FILTER_TABLE = "**,order[id],receipt[**,lastPerson[id,nickname,name]]";

    @Autowired
    private OrderReceiptService orderReceiptService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_CREATE')")
    public String create(@RequestBody OrderReceipt orderReceipt, Principal principal) {
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
        orderReceipt = orderReceiptService.save(orderReceipt);
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "السندات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء السند بنجاح" : "Create OrderReceipt Account Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderReceipt);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        OrderReceipt orderOrderReceipt = orderReceiptService.findOne(id);
        if (orderOrderReceipt != null) {
            receiptService.delete(orderOrderReceipt.getReceipt());
            orderReceiptService.delete(orderOrderReceipt);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "السندات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف السند وكل ما يتعلق به من حسابات بنجاح" : "Delete OrderReceipt Account With All Related Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
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
}
