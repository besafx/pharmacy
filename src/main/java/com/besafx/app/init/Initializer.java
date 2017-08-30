package com.besafx.app.init;

import com.besafx.app.entity.Company;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Team;
import com.besafx.app.service.CompanyService;
import com.besafx.app.service.PersonService;
import com.besafx.app.service.TeamService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {

    private final static Logger log = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private TeamService teamService;

    @Autowired
    private PersonService personService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (personService.count() == 0) {
            runForFirstTimeOnly();
        }
    }

    private void runForFirstTimeOnly() {
        log.info("انشاء مجموعة الصلاحيات");
        Team team = new Team();
        team.setCode(1);
        team.setName("الدعم الفني");
        team.setAuthorities(String.join(",",
                "ROLE_COMPANY_UPDATE",
                "ROLE_CUSTOMER_CREATE",
                "ROLE_CUSTOMER_UPDATE",
                "ROLE_CUSTOMER_DELETE",
                "ROLE_CUSTOMER_ENABLE",
                "ROLE_CUSTOMER_DISABLE",
                "ROLE_DOCTOR_CREATE",
                "ROLE_DOCTOR_UPDATE",
                "ROLE_DOCTOR_DELETE",
                "ROLE_DOCTOR_ENABLE",
                "ROLE_DOCTOR_DISABLE",
                "ROLE_EMPLOYEE_CREATE",
                "ROLE_EMPLOYEE_UPDATE",
                "ROLE_EMPLOYEE_DELETE",
                "ROLE_EMPLOYEE_ENABLE",
                "ROLE_EMPLOYEE_DISABLE",
                "ROLE_DETECTION_TYPE_CREATE",
                "ROLE_DETECTION_TYPE_UPDATE",
                "ROLE_DETECTION_TYPE_DELETE",
                "ROLE_FALCON_CREATE",
                "ROLE_FALCON_UPDATE",
                "ROLE_FALCON_DELETE",
                "ROLE_DRUG_CREATE",
                "ROLE_DRUG_UPDATE",
                "ROLE_DRUG_DELETE",
                "ROLE_DRUG_CATEGORY_CREATE",
                "ROLE_DRUG_CATEGORY_UPDATE",
                "ROLE_DRUG_CATEGORY_DELETE",
                "ROLE_TEAM_CREATE",
                "ROLE_TEAM_UPDATE",
                "ROLE_TEAM_DELETE",
                "ROLE_PROFILE_UPDATE"
        ));
        teamService.save(team);
        //
        log.info("إنشاء المستخدم الخاص بمدير النظام");
        Person person = new Person();
        person.setNickname("Eng.");
        person.setName("BASSAM ALMAHDY");
        person.setPhoto("");
        person.setQualification("Web Developer");
        person.setEmail("islamhaker@gmail.com");
        person.setPassword(passwordEncoder.encode("besa2009"));
        person.setHiddenPassword("besa2009");
        person.setEnabled(true);
        person.setTokenExpired(false);
        person.setActive(false);
        person.setTechnicalSupport(true);
        person.setTeam(team);
        person.setOptions(JSONConverter.toString(Options.builder().lang("AR").dateType("H")));
        log.info(JSONConverter.toString(Options.builder().lang("AR").dateType("H")));
        personService.save(person);
        //
        log.info("انشاء الشركة");
        Company company = new Company();
        company.setCode(1);
        company.setName("الصيدلية الذكية");
        company.setPhone("0138099353");
        company.setFax("0138099352");
        company.setEmail("info@ararhni.com");
        company.setWebsite("www.ararhni.com");
        companyService.save(company);
    }
}
