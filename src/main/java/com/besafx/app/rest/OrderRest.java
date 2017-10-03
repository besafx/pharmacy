package com.besafx.app.rest;

import com.besafx.app.config.DropboxManager;
import com.besafx.app.entity.Order;
import com.besafx.app.entity.OrderAttach;
import com.besafx.app.entity.OrderDetectionType;
import com.besafx.app.entity.Person;
import com.besafx.app.search.OrderSearch;
import com.besafx.app.service.*;
import com.besafx.app.util.DateConverter;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.util.WrapperUtil;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.bouncycastle.jce.provider.symmetric.AES;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.beans.Transient;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping(value = "/api/order/")
public class OrderRest {

    private final Logger log = LoggerFactory.getLogger(OrderRest.class);

    public static final String FILTER_TABLE = "**,falcon[**,customer[id,code,name]],doctor[**,person[id,code,name,mobile,identityNumber]],diagnoses[**,-order,drug[**,-drugCategory,-transactionBuys],drugUnit[id,name]],orderDetectionTypes[**,-order,orderDetectionTypeAttaches[id]],orderAttaches[**,attach[**,person[id,nickname,name]],-order]";
    public static final String FILTER_ORDER_COMBO = "id,code";

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSearch orderSearch;

    @Autowired
    private OrderDetectionTypeService orderDetectionTypeService;

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private OrderAttachService orderAttachService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DropboxManager dropboxManager;

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
    public void delete(@PathVariable Long id, Principal principal) throws Exception {
        Order order = orderService.findOne(id);
        if (order != null) {
            diagnosisService.delete(order.getDiagnoses());
            orderDetectionTypeService.delete(order.getOrderDetectionTypes());
            ListIterator<OrderAttach> listIterator = order.getOrderAttaches().listIterator();
            while (listIterator.hasNext()){
                OrderAttach orderAttach = listIterator.next();
                dropboxManager.deleteFile("/Pharmacy4Falcon/Orders/" + orderAttach.getOrder().getId() + "/" + orderAttach.getAttach().getName() + "." + orderAttach.getAttach().getMimeType()).get();
                orderAttachService.delete(orderAttach);
            }
            orderService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                    .message(lang.equals("AR") ? "تم حذف الطلب وكل ما يتعلق به من مستندات ونتائج فحص بنجاح" : "Delete Order Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "saveNote/{id}/{note}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_SAVE_NOTE')")
    @Transactional
    public void saveNote(@PathVariable(value = "id") Long id, @PathVariable(value = "note") String note, Principal principal) {
        Order order = orderService.findOne(id);
        if (order != null) {
            order.setNote(note);
            orderService.save(order);
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Order> list = Lists.newArrayList(orderService.findAll());
        list.sort(Comparator.comparing(Order::getCode).reversed());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Order> list = Lists.newArrayList(orderService.findAll());
        list.sort(Comparator.comparing(Order::getCode).reversed());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_ORDER_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderService.findOne(id));
    }

    @RequestMapping(value = "findQuantityByDay", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findQuantityByDay() {
        List<WrapperUtil> list = new ArrayList<>();
        DateConverter.getDaysOfThisWeek().stream().forEach(interval -> {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1(interval.getStart().dayOfWeek().getAsText(Locale.forLanguageTag("ar")));
            wrapperUtil.setObj2(orderService.countByDateBetween(interval.getStart().toDate(), interval.getEnd().toDate()));
            list.add(wrapperUtil);
        });
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), "obj1,obj2"), list);
    }

    @RequestMapping(value = "findQuantityByMonth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findQuantityByMonth() {
        List<WrapperUtil> list = new ArrayList<>();
        DateConverter.getMonthsOfThisYear().stream().forEach(interval -> {
            WrapperUtil wrapperUtil = new WrapperUtil();
            wrapperUtil.setObj1(interval.getStart().monthOfYear().getAsText(Locale.forLanguageTag("ar")));
            wrapperUtil.setObj2(orderService.countByDateBetween(interval.getStart().toDate(), interval.getEnd().toDate()));
            list.add(wrapperUtil);
        });
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), "obj1,obj2"), list);
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "customerName", required = false) final String customerName,
            @RequestParam(value = "customerMobile", required = false) final String customerMobile,
            @RequestParam(value = "customerIdentityNumber", required = false) final String customerIdentityNumber,
            @RequestParam(value = "falconCode", required = false) final Long falconCode,
            @RequestParam(value = "falconType", required = false) final String falconType,
            @RequestParam(value = "weightFrom", required = false) final Double weightFrom,
            @RequestParam(value = "weightTo", required = false) final Double weightTo,
            @RequestParam(value = "doctorName", required = false) final String doctorName,
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
        List<Order> list = orderSearch.filter(codeFrom, codeTo, dateFrom, dateTo, customerName, customerMobile, customerIdentityNumber, falconCode, falconType, weightFrom, weightTo, doctorName);
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
