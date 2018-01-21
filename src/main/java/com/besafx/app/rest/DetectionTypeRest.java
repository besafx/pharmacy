package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.DetectionType;
import com.besafx.app.entity.Person;
import com.besafx.app.service.DetectionTypeService;
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

@RestController
@RequestMapping(value = "/api/detectionType/")
public class DetectionTypeRest {

    public static final String FILTER_TABLE = "**,-orderDetectionTypes,-orders";
    public static final String FILTER_DETECTION_TYPE_COMBO = "id,code,nameArabic,nameEnglish,cost";

    @Autowired
    private DetectionTypeService detectionTypeService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DETECTION_TYPE_CREATE')")
    @Transactional
    public String create(@RequestBody DetectionType detectionType, Principal principal) {
        DetectionType topDetectionType = detectionTypeService.findTopByOrderByCodeDesc();
        if (topDetectionType == null) {
            detectionType.setCode(1);
        } else {
            detectionType.setCode(topDetectionType.getCode() + 1);
        }
        detectionType = detectionTypeService.save(detectionType);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العمليات على انواع الفحوصات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء نوع فحص جديد بنجاح" : "Create Detection Type Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), detectionType);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DETECTION_TYPE_UPDATE')")
    @Transactional
    public String update(@RequestBody DetectionType detectionType, Principal principal) {
        if (detectionTypeService.findByCodeAndIdIsNot(detectionType.getCode(), detectionType.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        DetectionType object = detectionTypeService.findOne(detectionType.getId());
        if (object != null) {
            detectionType = detectionTypeService.save(detectionType);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على انواع الفحوصات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل نوع الفحص بنجاح" : "Update Detection Type Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), detectionType);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DETECTION_TYPE_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        DetectionType detectionType = detectionTypeService.findOne(id);
        if (detectionType != null) {
            detectionTypeService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على انواع الفحوصات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف نوع فحص بنجاح" : "Delete Detection Type Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<DetectionType> list = Lists.newArrayList(detectionTypeService.findAll());
        list.sort(Comparator.comparing(DetectionType::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<DetectionType> list = Lists.newArrayList(detectionTypeService.findAll());
        list.sort(Comparator.comparing(DetectionType::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DETECTION_TYPE_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), detectionTypeService.findOne(id));
    }
}
