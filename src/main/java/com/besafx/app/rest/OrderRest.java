package com.besafx.app.rest;

import com.besafx.app.entity.Order;
import com.besafx.app.entity.OrderDetectionType;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.enums.OrderCondition;
import com.besafx.app.search.OrderSearch;
import com.besafx.app.service.OrderDetectionTypeService;
import com.besafx.app.service.OrderService;
import com.besafx.app.service.PersonService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/order/")
public class OrderRest {

    public static final String FILTER_TABLE = "**,falcon[**,customer[id,code,name]],doctor[**,person[id,code,name,mobile,identityNumber]],orderDetectionTypes[**,-order]";
    public static final String FILTER_ORDER_COMBO = "**,falcon[id,customer[id,name]],doctor[id,person[id,name]]";
    private final Logger log = LoggerFactory.getLogger(OrderRest.class);
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSearch orderSearch;

    @Autowired
    private OrderDetectionTypeService orderDetectionTypeService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_CREATE')")
    @Transactional
    public String create(@RequestBody Order order, Principal principal) {
        Order topOrder = orderService.findTopByOrderByCodeDesc();
        if (topOrder == null) {
            order.setCode(1);
        } else {
            order.setCode(topOrder.getCode() + 1);
        }
        order.setOrderCondition(OrderCondition.Pending);
        order.setDate(new DateTime().toDate());
        order = orderService.save(order);
        ListIterator<OrderDetectionType> listIterator = order.getOrderDetectionTypes().listIterator();
        while (listIterator.hasNext()) {
            OrderDetectionType orderDetectionType = listIterator.next();
            orderDetectionType.setOrder(order);
            listIterator.set(orderDetectionTypeService.save(orderDetectionType));
        }
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "تم انشاء طلب جديد بنجاح" : "Create Order Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), order);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        Order order = orderService.findOne(id);
        if (order != null) {
            orderDetectionTypeService.delete(order.getOrderDetectionTypes());
            orderService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                    .message(lang.equals("AR") ? "تم حذف الطلب بنجاح" : "Delete Order Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Order> list = Lists.newArrayList(orderService.findAll());
        list.sort(Comparator.comparing(Order::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Order> list = Lists.newArrayList(orderService.findAll());
        list.sort(Comparator.comparing(Order::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_ORDER_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderService.findOne(id));
    }

    @RequestMapping(value = "findPending", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findPending() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                orderService.findByOrderConditionIn(Lists.newArrayList(OrderCondition.Pending))
                        .stream().sorted(Comparator.comparing(Order::getCode))
                        .collect(Collectors.toList()));
    }

    @RequestMapping(value = "findDiagnosed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findDiagnosed() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                orderService.findByOrderConditionIn(Lists.newArrayList(OrderCondition.Diagnosed))
                        .stream().sorted(Comparator.comparing(Order::getCode))
                        .collect(Collectors.toList()));
    }

    @RequestMapping(value = "findDone", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findDone() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                orderService.findByOrderConditionIn(Lists.newArrayList(OrderCondition.Done))
                        .stream().sorted(Comparator.comparing(Order::getCode))
                        .collect(Collectors.toList()));
    }

    @RequestMapping(value = "findCanceled", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findCanceled() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                orderService.findByOrderConditionIn(Lists.newArrayList(OrderCondition.Canceled))
                        .stream().sorted(Comparator.comparing(Order::getCode))
                        .collect(Collectors.toList()));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "orderConditions", required = false) final List<OrderCondition> orderConditions,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "falcons", required = false) final List<Long> falcons,
            @RequestParam(value = "doctors", required = false) final List<Long> doctors,
            Principal principal) {
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "جاري تصفية النتائج، فضلاً انتظر قليلا..." : "Filtering Data")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        List<Order> list = orderSearch.filter(codeFrom, codeTo, orderConditions, dateFrom, dateTo, falcons, doctors);
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "تمت العملية بنجاح" : "job Done")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }
}
