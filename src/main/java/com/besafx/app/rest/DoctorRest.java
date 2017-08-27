package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Doctor;
import com.besafx.app.entity.Person;
import com.besafx.app.service.DoctorService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/doctor/")
public class DoctorRest {

    private final static Logger log = LoggerFactory.getLogger(DoctorRest.class);

    public static final String FILTER_TABLE = "**,person[**,-hiddenPassword,team[id]]";
    public static final String FILTER_DOCTOR_COMBO = "**,person[id,nickname,name]";

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_CREATE')")
    @Transactional
    public String create(@RequestBody Doctor doctor, Principal principal) {
        if (personService.findByEmail(doctor.getPerson().getEmail()) != null) {
            throw new CustomException("هذا البريد الإلكتروني غير متاح ، فضلاً ادخل بريد آخر غير مستخدم");
        }
        doctor.getPerson().setHiddenPassword(doctor.getPerson().getPassword());
        doctor.getPerson().setPassword(passwordEncoder.encode(doctor.getPerson().getPassword()));
        doctor.getPerson().setEnabled(true);
        doctor.getPerson().setTokenExpired(false);
        doctor.getPerson().setActive(false);
        doctor.getPerson().setTechnicalSupport(false);
        doctor.getPerson().setOptions(JSONConverter.toString(Options.builder().lang("AR").dateType("H")));
        doctor.setPerson(personService.save(doctor.getPerson()));
        Doctor topDoctor = doctorService.findTopByOrderByCodeDesc();
        if (topDoctor == null) {
            doctor.setCode(1);
        } else {
            doctor.setCode(topDoctor.getCode() + 1);
        }
        doctor = doctorService.save(doctor);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العمليات على حسابات الاطباء" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء حساب الطبيب بنجاح" : "Create Doctor Account Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), doctor);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_UPDATE')")
    @Transactional
    public String update(@RequestBody Doctor doctor, Principal principal) {
        if (doctorService.findByCodeAndIdIsNot(doctor.getCode(), doctor.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Doctor object = doctorService.findOne(doctor.getId());
        if (object != null) {
            log.info("فحص الباسوورد الخاص بحساب الطبيب...");
            if (!object.getPerson().getPassword().equals(doctor.getPerson().getPassword())) {
                doctor.getPerson().setHiddenPassword(doctor.getPerson().getPassword());
                doctor.getPerson().setPassword(passwordEncoder.encode(doctor.getPerson().getPassword()));
            } else {
                doctor.getPerson().setHiddenPassword(object.getPerson().getHiddenPassword());
            }
            doctor.getPerson().setTokenExpired(false);
            doctor.getPerson().setActive(false);
            doctor.getPerson().setTechnicalSupport(false);
            log.info("حفظ حساب الطبيب...");
            doctor.setPerson(personService.save(doctor.getPerson()));
            log.info("حفظ بيانات الطبيب...");
            doctor = doctorService.save(doctor);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على حسابات الاطباء" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات حساب الطبيب بنجاح" : "Update Doctor Account Information Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), doctor);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "setGUILang/{doctorId}/{lang}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_UPDATE')")
    @Transactional
    public String setGUILang(@PathVariable(value = "doctorId") Long doctorId, @PathVariable(value = "lang") String lang,  Principal principal) {
        Doctor doctor = doctorService.findOne(doctorId);
        Options options = JSONConverter.toObject(doctor.getPerson().getOptions(), Options.class);
        options.setLang(lang);
        doctor.getPerson().setOptions(JSONConverter.toString(options));
        doctor.setPerson(personService.save(doctor.getPerson()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), doctorService.save(doctor));
    }

    @RequestMapping(value = "setDateType/{doctorId}/{type}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_UPDATE')")
    @Transactional
    public String setDateType(@PathVariable(value = "doctorId") Long doctorId, @PathVariable(value = "type") String type,  Principal principal) {
        Doctor doctor = doctorService.findOne(doctorId);
        Options options = JSONConverter.toObject(doctor.getPerson().getOptions(), Options.class);
        options.setDateType(type);
        doctor.getPerson().setOptions(JSONConverter.toString(options));
        doctor.setPerson(personService.save(doctor.getPerson()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), doctorService.save(doctor));
    }

    @RequestMapping(value = "enable/{doctorId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_ENABLE')")
    @Transactional
    public String enable(@PathVariable(value = "doctorId") Long doctorId, Principal principal) {
        Doctor doctor = doctorService.findOne(doctorId);
        if (doctor != null) {
            doctor.getPerson().setEnabled(true);
            doctor.getPerson().setTokenExpired(false);
            doctor.getPerson().setActive(false);
            doctor.getPerson().setTechnicalSupport(false);
            doctor.setPerson(personService.save(doctor.getPerson()));
            doctor = doctorService.save(doctor);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), doctor);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "disable/{doctorId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_DISABLE')")
    @Transactional
    public String disable(@PathVariable(value = "doctorId") Long doctorId, Principal principal) {
        Doctor doctor = doctorService.findOne(doctorId);
        if (doctor != null) {
            doctor.getPerson().setEnabled(false);
            doctor.getPerson().setTokenExpired(false);
            doctor.getPerson().setActive(false);
            doctor.getPerson().setTechnicalSupport(false);
            doctor.setPerson(personService.save(doctor.getPerson()));
            doctor = doctorService.save(doctor);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), doctor);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        Doctor doctor = doctorService.findOne(id);
        if (doctor != null) {
            doctorService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على حسابات الاطباء" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف حساب الطبيب بنجاح" : "Delete Doctor Account Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Doctor> list = Lists.newArrayList(doctorService.findAll());
        list.sort(Comparator.comparing(Doctor::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Doctor> list = Lists.newArrayList(doctorService.findAll());
        list.sort(Comparator.comparing(Doctor::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_DOCTOR_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), doctorService.findOne(id));
    }
}
