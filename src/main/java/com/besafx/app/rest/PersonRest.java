package com.besafx.app.rest;
import com.besafx.app.config.CustomException;
import com.besafx.app.entity.*;
import com.besafx.app.service.*;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/person/")
public class PersonRest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PERSON_CREATE')")
    public Person create(@RequestBody Person person, Principal principal) {
        if (personService.findByEmail(person.getEmail()) != null) {
            throw new CustomException("هذا البريد الإلكتروني غير متاح ، فضلاً ادخل بريد آخر غير مستخدم");
        }
        person.setHiddenPassword(person.getPassword());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setEnabled(true);
        person.setTokenExpired(false);
        person.setActive(false);
        person.setTechnicalSupport(false);
        person.setOptions(JSONConverter.toString(Options.builder().lang("AR").dateType("H")));
        person = personService.save(person);
        notificationService.notifyOne(Notification
                .builder()
                .title("العمليات على حسابات المستخدمين")
                .message("تم اضافة مستخدم جديد بنجاح")
                .type("success")
                .icon("fa-plus-circle")
                .build(), principal.getName());
        return person;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PERSON_UPDATE') or hasRole('ROLE_PROFILE_UPDATE')")
    public Person update(@RequestBody Person person, Principal principal) {
        Person object = personService.findOne(person.getId());
        String lang = JSONConverter.toObject(person.getOptions(), Options.class).getLang();
        if (object != null) {
            if (!object.getPassword().equals(person.getPassword())) {
                person.setHiddenPassword(person.getPassword());
                person.setPassword(passwordEncoder.encode(person.getPassword()));
            } else {
                person.setHiddenPassword(object.getHiddenPassword());
            }
            person.setEnabled(true);
            person.setTokenExpired(false);
            person.setActive(false);
            person.setTechnicalSupport(false);
            person = personService.save(person);
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على قواعد البيانات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات الحساب بنجاح" : "Update account information successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return person;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "setGUILang/{lang}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PROFILE_UPDATE')")
    public void setGUILang(@PathVariable(value = "lang") String lang,  Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        Options options = JSONConverter.toObject(person.getOptions(), Options.class);
        options.setLang(lang);
        person.setOptions(JSONConverter.toString(options));
        personService.save(person);
    }

    @RequestMapping(value = "setDateType/{type}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PROFILE_UPDATE')")
    public void setDateType(@PathVariable(value = "type") String type,  Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        Options options = JSONConverter.toObject(person.getOptions(), Options.class);
        options.setDateType(type);
        person.setOptions(JSONConverter.toString(options));
        personService.save(person);
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PERSON_DELETE')")
    public void delete(@PathVariable Long id) {
        Person object = personService.findOne(id);
        if (object != null) {
            personService.delete(id);
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Person> findAll() {
        List<Person> list = Lists.newArrayList(personService.findAll());
        list.sort(Comparator.comparing(Person::getName));
        return list;
    }

    @RequestMapping(value = "findAllSummery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Person> findAllSummery() {
        return findAll();
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Person findOne(@PathVariable Long id) {
        return personService.findOne(id);
    }

    @RequestMapping(value = "findByEmail/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Person findByEmail(@PathVariable(value = "email") String email) {
        return personService.findByEmail(email);
    }

    @RequestMapping("findActivePerson")
    @ResponseBody
    public Person findActivePerson(Principal principal) {
        return personService.findByEmail(principal.getName());
    }

}
