package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.DrugCategory;
import com.besafx.app.entity.Person;
import com.besafx.app.service.DrugCategoryService;
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
@RequestMapping(value = "/api/drugCategory/")
public class DrugCategoryRest {

    public static final String FILTER_TABLE = "**";
    public static final String FILTER_DRUG_CATEGORY_COMBO = "id,code,nameArabic,nameEnglish";

    @Autowired
    private DrugCategoryService drugCategoryService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DRUG_CATEGORY_CREATE')")
    @Transactional
    public String create(@RequestBody DrugCategory drugCategory, Principal principal) {
        DrugCategory topDrugCategory = drugCategoryService.findTopByOrderByCodeDesc();
        if (topDrugCategory == null) {
            drugCategory.setCode(1);
        } else {
            drugCategory.setCode(topDrugCategory.getCode() + 1);
        }
        drugCategory = drugCategoryService.save(drugCategory);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العمليات على الصيدلية" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء تصنيف جديد بنجاح" : "Create Drug Category Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drugCategory);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DRUG_CATEGORY_UPDATE')")
    @Transactional
    public String update(@RequestBody DrugCategory drugCategory, Principal principal) {
        if (drugCategoryService.findByCodeAndIdIsNot(drugCategory.getCode(), drugCategory.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        DrugCategory object = drugCategoryService.findOne(drugCategory.getId());
        if (object != null) {
            drugCategory = drugCategoryService.save(drugCategory);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على الصيدلية" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات التصنيف بنجاح" : "Update Drug Category Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drugCategory);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DRUG_CATEGORY_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        DrugCategory drugCategory = drugCategoryService.findOne(id);
        if (drugCategory != null) {
            drugCategoryService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على الصيدلية" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف التصنيف بنجاح" : "Delete DrugCategory Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<DrugCategory> list = Lists.newArrayList(drugCategoryService.findAll());
        list.sort(Comparator.comparing(DrugCategory::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<DrugCategory> list = Lists.newArrayList(drugCategoryService.findAll());
        list.sort(Comparator.comparing(DrugCategory::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DRUG_CATEGORY_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), drugCategoryService.findOne(id));
    }
}
