package com.besafx.app.rest;

import com.besafx.app.config.DropboxManager;
import com.besafx.app.entity.*;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.search.OrderSearch;
import com.besafx.app.service.*;
import com.besafx.app.util.*;
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
import java.util.*;

@RestController
@RequestMapping(value = "/api/order/")
public class OrderRest {

    private final Logger log = LoggerFactory.getLogger(OrderRest.class);

    public static final String FILTER_TABLE = "**,orderReceipts[id,receipt[**,lastPerson[id,nickname,name]]],lastPerson[id,nickname,name],falcon[**,customer[id,code,name]],doctor[**,person[id,code,name,mobile,identityNumber]],diagnoses[**,-order,drug[**,-drugCategory,-transactionBuys],drugUnit[id,name]],orderDetectionTypes[**,-order,orderDetectionTypeAttaches[id]],orderAttaches[**,attach[**,person[id,nickname,name]],-order]";
    public static final String FILTER_TABLE_DEBT = "**,-orderReceipts,lastPerson[id,nickname,name],falcon[**,customer[id,code,name]],-doctor,-diagnoses,-orderDetectionTypes,-orderAttaches";
    public static final String FILTER_ORDER_COMBO = "id,code";

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSearch orderSearch;

    @Autowired
    private OrderDetectionTypeService orderDetectionTypeService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private OrderReceiptService orderReceiptService;

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
        Person caller = personService.findByEmail(principal.getName());
        Order topOrder = orderService.findTopByOrderByCodeDesc();
        if (topOrder == null) {
            order.setCode(1);
        } else {
            order.setCode(topOrder.getCode() + 1);
        }
        order.setDate(new DateTime().toDate());
        order.setLastUpdate(new DateTime().toDate());
        order.setLastPerson(caller);
        order = orderService.save(order);
        {
            ListIterator<OrderDetectionType> listIterator = order.getOrderDetectionTypes().listIterator();
            while (listIterator.hasNext()) {
                OrderDetectionType orderDetectionType = listIterator.next();
                orderDetectionType.setOrder(order);
                if(orderDetectionType.getDone() == null){
                    orderDetectionType.setDone(true);
                }
                listIterator.set(orderDetectionTypeService.save(orderDetectionType));
            }
        }
        {
            ListIterator<OrderReceipt> listIterator = order.getOrderReceipts().listIterator();
            while (listIterator.hasNext()) {
                OrderReceipt orderReceipt = listIterator.next();
                if(orderReceipt.getReceipt().getAmountNumber() == 0){
                    log.info("تجاهل إنشاء السند لقيمته الصفرية");
                    break;
                }
                //
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
                //
                orderReceipt.setOrder(order);
                listIterator.set(orderReceiptService.save(orderReceipt));
            }
        }

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
            while (listIterator.hasNext()) {
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

    @RequestMapping(value = "findByCustomer/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByCustomer(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderService.findByFalconCustomerId(id));
    }

    @RequestMapping(value = "findByFalcon/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByFalcon(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderService.findByFalconId(id));
    }

    @RequestMapping(value = "findByFalconAndCodeNot/{id}/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByFalconAndCodeNot(@PathVariable Long id, @PathVariable Integer code) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderService.findByFalconIdAndCodeNot(id, code));
    }

    @RequestMapping(value = "findByFalconCustomerAndCodeNot/{id}/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByFalconCustomerAndCodeNot(@PathVariable Long id, @PathVariable Integer code) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderService.findByFalconCustomerIdAndCodeNot(id, code));
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
            @RequestParam(value = "paymentMethods", required = false) final List<PaymentMethod> paymentMethods,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "customerName", required = false) final String customerName,
            @RequestParam(value = "customerMobile", required = false) final String customerMobile,
            @RequestParam(value = "customerIdentityNumber", required = false) final String customerIdentityNumber,
            @RequestParam(value = "falconCode", required = false) final Long falconCode,
            @RequestParam(value = "falconType", required = false) final String falconType,
            @RequestParam(value = "weightFrom", required = false) final Double weightFrom,
            @RequestParam(value = "weightTo", required = false) final Double weightTo,
            @RequestParam(value = "doctorName", required = false) final String doctorName) {
        List<Order> list = orderSearch.filter(codeFrom, codeTo, paymentMethods, dateFrom, dateTo, customerName, customerMobile, customerIdentityNumber, falconCode, falconType, weightFrom, weightTo, doctorName);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "filterDebt", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filterDebt(
            @RequestParam(value = "codeFrom", required = false) final Long codeFrom,
            @RequestParam(value = "codeTo", required = false) final Long codeTo,
            @RequestParam(value = "paymentMethods", required = false) final List<PaymentMethod> paymentMethods,
            @RequestParam(value = "dateFrom", required = false) final Long dateFrom,
            @RequestParam(value = "dateTo", required = false) final Long dateTo,
            @RequestParam(value = "customerName", required = false) final String customerName,
            @RequestParam(value = "customerMobile", required = false) final String customerMobile,
            @RequestParam(value = "customerIdentityNumber", required = false) final String customerIdentityNumber,
            @RequestParam(value = "falconCode", required = false) final Long falconCode,
            @RequestParam(value = "falconType", required = false) final String falconType,
            @RequestParam(value = "weightFrom", required = false) final Double weightFrom,
            @RequestParam(value = "weightTo", required = false) final Double weightTo,
            @RequestParam(value = "doctorName", required = false) final String doctorName) {
        List<Order> list = orderSearch.filter(codeFrom, codeTo, paymentMethods, dateFrom, dateTo, customerName, customerMobile, customerIdentityNumber, falconCode, falconType, weightFrom, weightTo, doctorName);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE_DEBT), list);
    }

    @RequestMapping(value = "findByToday", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByToday() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderSearch.findByToday());
    }

    @RequestMapping(value = "findByWeek", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByWeek() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderSearch.findByWeek());
    }

    @RequestMapping(value = "findByMonth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByMonth() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderSearch.findByMonth());
    }

    @RequestMapping(value = "findByYear", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByYear() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), orderSearch.findByYear());
    }
}
