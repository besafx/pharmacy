package com.besafx.app.init;
import com.besafx.app.entity.Company;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Team;
import com.besafx.app.service.CompanyService;
import com.besafx.app.service.PersonService;
import com.besafx.app.service.TeamService;
import com.besafx.app.util.AppOptions;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.util.WrapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                "ROLE_TEAM_CREATE",
                "ROLE_TEAM_UPDATE",
                "ROLE_TEAM_DELETE",
                "ROLE_PERSON_CREATE",
                "ROLE_PERSON_UPDATE",
                "ROLE_PERSON_DELETE",
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
