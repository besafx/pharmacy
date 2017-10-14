package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Doctor;
import com.besafx.app.entity.Employee;
import com.besafx.app.entity.Person;
import com.besafx.app.service.EmployeeService;
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

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/employee/")
public class EmployeeRest {

    private final static Logger log = LoggerFactory.getLogger(EmployeeRest.class);

    public static final String FILTER_TABLE = "**,salaries[id],person[**,-hiddenPassword,team[id]]";
    public static final String FILTER_EMPLOYEE_COMBO = "**,-salaries,person[id,nickname,name,mobile]";

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_CREATE')")
    @Transactional
    public String create(@RequestBody Employee employee, Principal principal) {
        if (personService.findByEmail(employee.getPerson().getEmail()) != null) {
            throw new CustomException("هذا البريد الإلكتروني غير متاح ، فضلاً ادخل بريد آخر غير مستخدم");
        }
        employee.getPerson().setHiddenPassword(employee.getPerson().getPassword());
        employee.getPerson().setPassword(passwordEncoder.encode(employee.getPerson().getPassword()));
        employee.getPerson().setEnabled(true);
        employee.getPerson().setTokenExpired(false);
        employee.getPerson().setActive(false);
        employee.getPerson().setTechnicalSupport(false);
        employee.getPerson().setOptions(JSONConverter.toString(Options.builder().lang("AR").dateType("H")));
        employee.setPerson(personService.save(employee.getPerson()));
        Employee topEmployee = employeeService.findTopByOrderByCodeDesc();
        if (topEmployee == null) {
            employee.setCode(1);
        } else {
            employee.setCode(topEmployee.getCode() + 1);
        }
        employee = employeeService.save(employee);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العمليات على حسابات الموظفون" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء حساب الموظف بنجاح" : "Create Employee Account Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), employee);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_UPDATE')")
    @Transactional
    public String update(@RequestBody Employee employee, Principal principal) {
        if (employeeService.findByCodeAndIdIsNot(employee.getCode(), employee.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Employee object = employeeService.findOne(employee.getId());
        if (object != null) {
            log.info("فحص الباسوورد الخاص بحساب الموظف...");
            if (!object.getPerson().getPassword().equals(employee.getPerson().getPassword())) {
                employee.getPerson().setHiddenPassword(employee.getPerson().getPassword());
                employee.getPerson().setPassword(passwordEncoder.encode(employee.getPerson().getPassword()));
            } else {
                employee.getPerson().setHiddenPassword(object.getPerson().getHiddenPassword());
            }
            employee.getPerson().setTokenExpired(false);
            employee.getPerson().setActive(false);
            employee.getPerson().setTechnicalSupport(false);
            log.info("حفظ حساب الموظف...");
            employee.setPerson(personService.save(employee.getPerson()));
            log.info("حفظ بيانات الموظف...");
            employee = employeeService.save(employee);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على حسابات الموظفون" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل بيانات حساب الموظف بنجاح" : "Update Employee Account Information Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), employee);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "setGUILang/{employeeId}/{lang}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_UPDATE')")
    @Transactional
    public String setGUILang(@PathVariable(value = "employeeId") Long employeeId, @PathVariable(value = "lang") String lang,  Principal principal) {
        Employee employee = employeeService.findOne(employeeId);
        Options options = JSONConverter.toObject(employee.getPerson().getOptions(), Options.class);
        options.setLang(lang);
        employee.getPerson().setOptions(JSONConverter.toString(options));
        employee.setPerson(personService.save(employee.getPerson()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), employeeService.save(employee));
    }

    @RequestMapping(value = "setDateType/{employeeId}/{type}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_UPDATE')")
    @Transactional
    public String setDateType(@PathVariable(value = "employeeId") Long employeeId, @PathVariable(value = "type") String type,  Principal principal) {
        Employee employee = employeeService.findOne(employeeId);
        Options options = JSONConverter.toObject(employee.getPerson().getOptions(), Options.class);
        options.setDateType(type);
        employee.getPerson().setOptions(JSONConverter.toString(options));
        employee.setPerson(personService.save(employee.getPerson()));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), employeeService.save(employee));
    }

    @RequestMapping(value = "enable/{employeeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_ENABLE')")
    @Transactional
    public String enable(@PathVariable(value = "employeeId") Long employeeId, Principal principal) {
        Employee employee = employeeService.findOne(employeeId);
        if (employee != null) {
            employee.getPerson().setEnabled(true);
            employee.getPerson().setTokenExpired(false);
            employee.getPerson().setActive(false);
            employee.getPerson().setTechnicalSupport(false);
            employee.setPerson(personService.save(employee.getPerson()));
            employee = employeeService.save(employee);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), employee);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "disable/{employeeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_DOCTOR_DISABLE')")
    @Transactional
    public String disable(@PathVariable(value = "employeeId") Long employeeId, Principal principal) {
        Employee employee = employeeService.findOne(employeeId);
        if (employee != null) {
            employee.getPerson().setEnabled(false);
            employee.getPerson().setTokenExpired(false);
            employee.getPerson().setActive(false);
            employee.getPerson().setTechnicalSupport(false);
            employee.setPerson(personService.save(employee.getPerson()));
            employee = employeeService.save(employee);
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), employee);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_EMPLOYEE_DELETE')")
    @Transactional
    public void delete(@PathVariable Long id, Principal principal) {
        Employee employee = employeeService.findOne(id);
        if (employee != null) {
            employeeService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على حسابات الموظفون" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف حساب الموظف بنجاح" : "Delete Employee Account Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Employee> list = Lists.newArrayList(employeeService.findAll());
        list.sort(Comparator.comparing(Employee::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Employee> list = Lists.newArrayList(employeeService.findAll());
        list.sort(Comparator.comparing(Employee::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_EMPLOYEE_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), employeeService.findOne(id));
    }
}
