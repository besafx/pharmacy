package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Falcon;
import com.besafx.app.entity.Person;
import com.besafx.app.service.FalconService;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/falcon/")
public class FalconRest {

    public static final String FILTER_TABLE = "**,customer[id,code,name,mobile]";
    public static final String FILTER_FALCON_COMBO = "id,code,type,customer[id,code,name,mobile]";

    @Autowired
    private FalconService falconService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_FALCON_CREATE')")
    @Transactional
    public String create(@RequestBody Falcon falcon, Principal principal) {
        if (falconService.findByCode(falcon.getCode()) != null) {
            throw new CustomException("هذا الكود مسجل بالفعل");
        }
        falcon.setRegisterDate(new Date());
        falcon = falconService.save(falcon);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العمليات على بيانات الصقور" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء حساب صقر جديد بنجاح" : "Create Falcon Account Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), falcon);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_FALCON_UPDATE')")
    @Transactional
    public String update(@RequestBody Falcon falcon, Principal principal) {
        if (falconService.findByCodeAndIdIsNot(falcon.getCode(), falcon.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Falcon object = falconService.findOne(falcon.getId());
        if (object != null) {
            falcon = falconService.save(falcon);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على بيانات الصقور" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات الصقر بنجاح" : "Update Falcon Account Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), falcon);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_FALCON_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        Falcon falcon = falconService.findOne(id);
        if (falcon != null) {
            falconService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على بيانات الصقور" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف حساب الصقر بنجاح" : "Delete Falcon Account Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Falcon> list = Lists.newArrayList(falconService.findAll());
        list.sort(Comparator.comparing(Falcon::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Falcon> list = Lists.newArrayList(falconService.findAll());
        list.sort(Comparator.comparing(Falcon::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_FALCON_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), falconService.findOne(id));
    }

    @RequestMapping(value = "findByCustomer/{customerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByCustomer(@PathVariable Long customerId) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), falconService.findByCustomerId(customerId));
    }
}
