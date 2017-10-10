package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Vacation;
import com.besafx.app.entity.Person;
import com.besafx.app.service.VacationService;
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
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/vacation/")
public class VacationRest {

    private final static Logger log = LoggerFactory.getLogger(VacationRest.class);

    public static final String FILTER_TABLE = "**,employee[id,person[id,nickname,name,mobile]]";

    @Autowired
    private VacationService vacationService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_VACATION_CREATE')")
    public String create(@RequestBody Vacation vacation, Principal principal) {
        if(vacation.getDays() >= vacation.getVacationType().getLimitInDays()){
            throw new CustomException("لا يمكن تعدي الحد الأقصي لعدد الايام");
        }
        vacation = vacationService.save(vacation);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "سجل الاجازات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء الاجازة بنجاح" : "Create Vacation Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), vacation);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_VACATION_UPDATE')")
    public String update(@RequestBody Vacation vacation, Principal principal) {
        Vacation object = vacationService.findOne(vacation.getId());
        if (object != null) {
            if(vacation.getDays() >= vacation.getVacationType().getLimitInDays()){
                throw new CustomException("لا يمكن تعدي الحد الأقصي لعدد الايام");
            }
            vacation = vacationService.save(vacation);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "سجل الاجازات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات الاجازة بنجاح" : "Update Vacation Information Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), vacation);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_VACATION_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        Vacation vacation = vacationService.findOne(id);
        if (vacation != null) {
            vacationService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "سجل الاجازات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف الاجازة بنجاح" : "Delete Vacation With All Related Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Vacation> list = Lists.newArrayList(vacationService.findAll());
        list.sort(Comparator.comparing(Vacation::getDate));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), vacationService.findOne(id));
    }
}
