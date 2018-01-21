package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Supplier;
import com.besafx.app.service.PersonService;
import com.besafx.app.service.SupplierService;
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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/supplier/")
public class SupplierRest {

    private final static Logger log = LoggerFactory.getLogger(SupplierRest.class);

    public static final String FILTER_TABLE = "**,billBuys[id]";
    public static final String FILTER_SUPPLIER_COMBO = "id,code,name,mobile";

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_CREATE')")
    public String create(@RequestBody Supplier supplier, Principal principal) {
        Supplier topSupplier = supplierService.findTopByOrderByCodeDesc();
        if (topSupplier == null) {
            supplier.setCode(1);
        } else {
            supplier.setCode(topSupplier.getCode() + 1);
        }
        supplier.setRegisterDate(new Date());
        supplier.setEnabled(true);
        supplier = supplierService.save(supplier);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العمليات على حسابات الموردين" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء حساب المورد بنجاح" : "Create Supplier Account Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplier);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_UPDATE')")
    public String update(@RequestBody Supplier supplier, Principal principal) {
        if (supplierService.findByCodeAndIdIsNot(supplier.getCode(), supplier.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Supplier object = supplierService.findOne(supplier.getId());
        if (object != null) {
            supplier = supplierService.save(supplier);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على حسابات الموردين" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات حساب المورد بنجاح" : "Update Supplier Account Information Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplier);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "enable/{supplierId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_ENABLE')")
    @Transactional
    public String enable(@PathVariable(value = "supplierId") Long supplierId, Principal principal) {
        Supplier supplier = supplierService.findOne(supplierId);
        if (supplier != null) {
            supplier.setEnabled(true);
            supplier = supplierService.save(supplier);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplier);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "disable/{supplierId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_DISABLE')")
    @Transactional
    public String disable(@PathVariable(value = "supplierId") Long supplierId, Principal principal) {
        Supplier supplier = supplierService.findOne(supplierId);
        if (supplier != null) {
            supplier.setEnabled(false);
            supplier = supplierService.save(supplier);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplier);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_SUPPLIER_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        Supplier supplier = supplierService.findOne(id);
        if (supplier != null) {
            supplierService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على حسابات الموردين" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف حساب المورد وكل ما يتعلق به من حسابات بنجاح" : "Delete Supplier Account With All Related Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Supplier> list = Lists.newArrayList(supplierService.findAll());
        list.sort(Comparator.comparing(Supplier::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Supplier> list = Lists.newArrayList(supplierService.findAll());
        list.sort(Comparator.comparing(Supplier::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_SUPPLIER_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), supplierService.findOne(id));
    }
}
