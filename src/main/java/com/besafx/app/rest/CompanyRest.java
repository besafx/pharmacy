package com.besafx.app.rest;

import com.besafx.app.entity.Company;
import com.besafx.app.entity.Person;
import com.besafx.app.service.CompanyService;
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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/company/")
public class CompanyRest {

    private final static Logger log = LoggerFactory.getLogger(CompanyRest.class);

    private final String FILTER_TABLE = "**";

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_COMPANY_UPDATE')")
    public String update(@RequestBody Company company, Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(person.getOptions(), Options.class).getLang();
        Company object = companyService.findOne(company.getId());
        if (object != null) {
            company = companyService.save(company);
            notificationService.notifyOne(Notification.builder()
                    .message(lang.equals("AR") ? "تم تعديل بيانات الشركة بنجاح" : "Update company information successfully")
                    .type("warning")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), company);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String get() {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), Lists.newArrayList(companyService.findAll()).get(0));
    }
}
