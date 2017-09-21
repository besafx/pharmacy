package com.besafx.app.rest;

import com.besafx.app.entity.Diagnosis;
import com.besafx.app.entity.Person;
import com.besafx.app.service.DiagnosisService;
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
@RequestMapping(value = "/api/diagnosis/")
public class DiagnosisRest {

    public static final String FILTER_TABLE = "**,orderDetectionType[**,order[id]],transactionSell[**,drugUnit[**,-drugUnit],transactionBuy[**,drugUnit[**,-drugUnit],drug[**,-drugCategory,-transactionBuys],billBuy[id,code],-transactionSells],billSell[id,code]]";

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DIAGNOSIS_CREATE')")
    @Transactional
    public String create(@RequestBody Diagnosis diagnosis, Principal principal) {
        Diagnosis topDiagnosis = diagnosisService.findTopByOrderByCodeDesc();
        if (topDiagnosis == null) {
            diagnosis.setCode(1);
        } else {
            diagnosis.setCode(topDiagnosis.getCode() + 1);
        }
        diagnosis.setDate(new Date());
        diagnosis = diagnosisService.save(diagnosis);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العيادة البيطرية" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء الوصفة الطبية بنجاح" : "Create Diagnosis Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), diagnosis);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DIAGNOSIS_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        Diagnosis diagnosis = diagnosisService.findOne(id);
        if (diagnosis != null) {
            diagnosisService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العيادة البيطرية" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف الوصفة الطبية بنجاح" : "Delete Diagnosis Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Diagnosis> list = Lists.newArrayList(diagnosisService.findAll());
        list.sort(Comparator.comparing(Diagnosis::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), diagnosisService.findOne(id));
    }

    @RequestMapping(value = "findByOrderDetectionTypeId/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByOrderDetectionTypeId(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE),
                diagnosisService.findByOrderDetectionTypeId(id));
    }
}
