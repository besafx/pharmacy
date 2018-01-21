package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Customer;
import com.besafx.app.entity.Person;
import com.besafx.app.search.CustomerSearch;
import com.besafx.app.service.CustomerService;
import com.besafx.app.service.FalconService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/customer/")
public class CustomerRest {

    public static final String FILTER_TABLE = "**,-orders,falcons[**,-orders,customer[id,code,name,mobile,identityNumber]]";
    public static final String FILTER_CUSTOMER_INFO = "id,code,nickname,name,registerDate,mobile,identityNumber,nationality,job,enabled";
    public static final String FILTER_CUSTOMER_COMBO = "id,code,nickname,name,mobile,identityNumber";

    private final static Logger log = LoggerFactory.getLogger(CustomerRest.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerSearch customerSearch;

    @Autowired
    private FalconService falconService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_CREATE')")
    public String create(@RequestBody Customer customer, Principal principal) {
        Customer topCustomer = customerService.findTopByOrderByCodeDesc();
        if (topCustomer == null) {
            customer.setCode(1);
        } else {
            customer.setCode(topCustomer.getCode() + 1);
        }
        customer.setRegisterDate(new Date());
        customer.setEnabled(true);
        customer = customerService.save(customer);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العمليات على حسابات العملاء" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء حساب العميل بنجاح" : "Create Customer Account Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customer);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_UPDATE')")
    public String update(@RequestBody Customer customer, Principal principal) {
        if (customerService.findByCodeAndIdIsNot(customer.getCode(), customer.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Customer object = customerService.findOne(customer.getId());
        if (object != null) {
            customer = customerService.save(customer);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على حسابات العملاء" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات حساب العميل بنجاح" : "Update Customer Account Information Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customer);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "enable/{customerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_ENABLE')")
    @Transactional
    public String enable(@PathVariable(value = "customerId") Long customerId, Principal principal) {
        Customer customer = customerService.findOne(customerId);
        if (customer != null) {
            customer.setEnabled(true);
            customer = customerService.save(customer);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customer);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "disable/{customerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_DISABLE')")
    @Transactional
    public String disable(@PathVariable(value = "customerId") Long customerId, Principal principal) {
        Customer customer = customerService.findOne(customerId);
        if (customer != null) {
            customer.setEnabled(false);
            customer = customerService.save(customer);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customer);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        Customer customer = customerService.findOne(id);
        if (customer != null) {
            orderService.findByFalconIn(customer.getFalcons()).stream().forEach(order -> {
                order.setFalcon(null);
                orderService.save(order);
            });
            falconService.delete(customer.getFalcons());
            customerService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على حسابات العملاء" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف حساب العميل وكل ما يتعلق به من حسابات بنجاح" : "Delete Customer Account With All Related Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Customer> list = Lists.newArrayList(customerService.findAll());
        list.sort(Comparator.comparing(Customer::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllInfo() {
        List<Customer> list = Lists.newArrayList(customerService.findAll());
        list.sort(Comparator.comparing(Customer::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_CUSTOMER_INFO), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Customer> list = Lists.newArrayList(customerService.findAll());
        list.sort(Comparator.comparing(Customer::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_CUSTOMER_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), customerService.findOne(id));
    }

    @RequestMapping(value = "filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String filter(
            @RequestParam(value = "code", required = false) final String code,
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "mobile", required = false) final String mobile,
            @RequestParam(value = "identityNumber", required = false) final String identityNumber,
            @RequestParam(value = "email", required = false) final String email) {
        List<Customer> list = customerSearch.filter(code, name, mobile, identityNumber, email);
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_CUSTOMER_INFO), list);
    }
}
