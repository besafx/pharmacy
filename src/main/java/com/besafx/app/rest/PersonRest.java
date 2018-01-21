package com.besafx.app.rest;

import com.besafx.app.entity.Person;
import com.besafx.app.service.PersonService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/person/")
public class PersonRest {

    public static final String FILTER_TABLE = "**,-hiddenPassword,team[**,-persons]";

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "setGUILang/{lang}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PROFILE_UPDATE')")
    public void setGUILang(@PathVariable(value = "lang") String lang, Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        Options options = JSONConverter.toObject(person.getOptions(), Options.class);
        options.setLang(lang);
        person.setOptions(JSONConverter.toString(options));
        personService.save(person);
    }

    @RequestMapping(value = "setDateType/{type}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PROFILE_UPDATE')")
    public void setDateType(@PathVariable(value = "type") String type, Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        Options options = JSONConverter.toObject(person.getOptions(), Options.class);
        options.setDateType(type);
        person.setOptions(JSONConverter.toString(options));
        personService.save(person);
    }

    @RequestMapping(value = "setStyle/{style}", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_PROFILE_UPDATE')")
    public void setStyle(@PathVariable(value = "style") String style, Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        Options options = JSONConverter.toObject(person.getOptions(), Options.class);
        options.setStyle(style);
        person.setOptions(JSONConverter.toString(options));
        personService.save(person);
    }

    @RequestMapping("findActivePerson")
    @ResponseBody
    public String findActivePerson(Principal principal) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), personService.findByEmail(principal.getName()));
    }

}
