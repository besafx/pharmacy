package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.enums.PersonType;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/person/")
public class PersonRest {

    public static final String FILTER_ALL = "**";
    public static final String FILTER_TABLE = "**,-password,-hiddenPassword,team[id,code,name,authorities]";
    public static final String FILTER_PERSON_COMBO = "id,nickname,name";

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_CREATE') or hasRole('ROLE_DOCTOR_CREATE') or hasRole('ROLE_EMPLOYEE_CREATE')")
    public String create(@RequestBody Person person, Principal principal) {
        if(!person.getPersonType().equals(PersonType.Customer)){
            if (personService.findByEmail(person.getEmail()) != null) {
                throw new CustomException("هذا البريد الإلكتروني غير متاح ، فضلاً ادخل بريد آخر غير مستخدم");
            }
            person.setHiddenPassword(person.getPassword());
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            person.setTokenExpired(false);
            person.setActive(false);
            person.setTechnicalSupport(false);
            person.setOptions(JSONConverter.toString(Options.builder().lang("AR").dateType("H")));
        }
        person.setEnabled(true);
        person = personService.save(person);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العمليات على قواعد البيانات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء الحساب بنجاح" : "Create Account successfully")
                .type("warning")
                .icon("fa-edit")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), person);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_CUSTOMER_UPDATE') or hasRole('ROLE_CUSTOMER_UPDATE') or hasRole('ROLE_CUSTOMER_UPDATE') or hasRole('ROLE_PROFILE_UPDATE')")
    public String update(@RequestBody Person person, Principal principal) {
        Person object = personService.findOne(person.getId());
        if (object != null) {
            if(!object.getPersonType().equals(PersonType.Customer)){
                if (!object.getPassword().equals(person.getPassword())) {
                    person.setHiddenPassword(person.getPassword());
                    person.setPassword(passwordEncoder.encode(person.getPassword()));
                } else {
                    person.setHiddenPassword(object.getHiddenPassword());
                }
                person.setTokenExpired(false);
                person.setActive(false);
                person.setTechnicalSupport(false);
            }
            person = personService.save(person);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على قواعد البيانات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات الحساب بنجاح" : "Update Account Information Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), person);
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
    public String findAll() {
        List<Person> list = Lists.newArrayList(personService.findAll((root, cq, cb) -> cb.isTrue(root.get("enabled"))));
        list.sort(Comparator.comparing(Person::getName));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), personService.findOne(id));
    }

    @RequestMapping(value = "findByEmail/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByEmail(@PathVariable(value = "email") String email) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), personService.findByEmail(email));
    }

    @RequestMapping(value = "findByPersonType/{personType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByPersonType(@PathVariable(value = "personType") PersonType personType) {
        Specification result = Specifications
                .where((root, cq, cb) -> cb.isTrue(root.get("enabled")))
                .and((root, cq, cb) -> cb.equal(root.get("personType"), personType));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), personService.findAll(result));
    }

    @RequestMapping(value = "findByPersonTypeCombo/{personType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findByPersonTypeCombo(@PathVariable(value = "personType") PersonType personType) {
        Specification result = Specifications
                .where((root, cq, cb) -> cb.isTrue(root.get("enabled")))
                .and((root, cq, cb) -> cb.equal(root.get("personType"), personType));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_PERSON_COMBO), personService.findAll(result));
    }

    @RequestMapping("findActivePerson")
    @ResponseBody
    public String findActivePerson(Principal principal) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), personService.findByEmail(principal.getName()));
    }

}
