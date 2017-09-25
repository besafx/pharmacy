package com.besafx.app.rest;

import com.besafx.app.entity.*;
import com.besafx.app.entity.enums.OrderCondition;
import com.besafx.app.search.OrderDetectionTypeSearch;
import com.besafx.app.service.DiagnosisService;
import com.besafx.app.service.OrderDetectionTypeService;
import com.besafx.app.service.PersonService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/orderDetectionType/")
public class OrderDetectionTypeRest {

    public static final String FILTER_TABLE = "**,orderDetectionTypeAttaches[id],diagnoses[**,-orderDetectionType,drug[id,code],drugUnit[**,-drugUnit]],order[**,falcon[id,code,customer[id,name]],doctor[id,person[id,code,nickname,name]],-orderDetectionTypes,-orderAttaches]";
    public static final String FILTER_ORDER_DETECTION_TYPE_COMBO = "**,diagnoses[id],order[id,code,orderCondition]";

    @Autowired
    private OrderDetectionTypeService orderDetectionTypeService;

    @Autowired
    private OrderDetectionTypeSearch orderDetectionTypeSearch;

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_CREATE')")
    @Transactional
    public String create(@RequestBody OrderDetectionType orderDetectionType, Principal principal) {
        orderDetectionType = orderDetectionTypeService.save(orderDetectionType);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "تم اضافة الفحص الجديد الي الطلب بنجاح" : "Adding Detection To Order Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderDetectionType);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ORDER_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        OrderDetectionType orderDetectionType = orderDetectionTypeService.findOne(id);
        if (orderDetectionType != null) {
            diagnosisService.delete(orderDetectionType.getDiagnoses());
            orderDetectionTypeService.delete(orderDetectionType);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "الاستقبال" : "The Sales")
                    .message(lang.equals("AR") ? "تم حذف الفحص وكل ما يتعلق به من وصفات طبية بنجاح" : "Delete Detection With All Related Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderDetectionTypeService.findOne(id));
    }

    @RequestMapping(value = "findPending", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findPending() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_ORDER_DETECTION_TYPE_COMBO),
                orderDetectionTypeService.findByOrderOrderConditionIn(Lists.newArrayList(OrderCondition.Pending))
                        .stream().sorted(Comparator.comparing(orderDetectionType -> orderDetectionType.getOrder().getCode()))
                        .collect(Collectors.toList()));
    }

    @RequestMapping(value = "findDiagnosed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findDiagnosed() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_ORDER_DETECTION_TYPE_COMBO),
                orderDetectionTypeService.findByOrderOrderConditionIn(Lists.newArrayList(OrderCondition.Diagnosed))
                        .stream().sorted(Comparator.comparing(orderDetectionType -> orderDetectionType.getOrder().getCode()))
                        .collect(Collectors.toList()));
    }

    @RequestMapping(value = "findDone", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findDone() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_ORDER_DETECTION_TYPE_COMBO),
                orderDetectionTypeService.findByOrderOrderConditionIn(Lists.newArrayList(OrderCondition.Done))
                        .stream().sorted(Comparator.comparing(orderDetectionType -> orderDetectionType.getOrder().getCode()))
                        .collect(Collectors.toList()));
    }

    @RequestMapping(value = "findCanceled", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findCanceled() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_ORDER_DETECTION_TYPE_COMBO),
                orderDetectionTypeService.findByOrderOrderConditionIn(Lists.newArrayList(OrderCondition.Canceled))
                        .stream().sorted(Comparator.comparing(orderDetectionType -> orderDetectionType.getOrder().getCode()))
                        .collect(Collectors.toList()));
    }

    @RequestMapping(value = "findByOrder/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByOrder(@PathVariable(value = "orderId") Long orderId, Principal principal) {
        List<OrderDetectionType> list = orderDetectionTypeService.findByOrderId(orderId);
        list.sort(Comparator.comparing(orderDetectionType -> orderDetectionType.getDetectionType().getCode()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_ORDER_DETECTION_TYPE_COMBO), list);
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "orderConditions", required = false) final List<OrderCondition> orderConditions,
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
        List<OrderDetectionType> list = orderDetectionTypeSearch.filter(codeFrom, codeTo, orderConditions, dateFrom, dateTo, customerName, customerMobile, customerIdentityNumber, falconCode, falconType, weightFrom, weightTo, doctorName);
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة الطبية" : "Clinic")
                .message(lang.equals("AR") ? "تمت العملية بنجاح" : "job Done")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_ORDER_DETECTION_TYPE_COMBO), list);
    }
}
