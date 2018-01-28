package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Person;
import com.besafx.app.entity.Team;
import com.besafx.app.service.PersonService;
import com.besafx.app.service.TeamService;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/team/")
public class TeamRest {

    public static final String FILTER_TABLE = "**,persons[id]";
    public static final String FILTER_TEAM_COMBO = "**,-persons";

    @Autowired
    private TeamService teamService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_TEAM_CREATE')")
    public String create(@RequestBody Team team, Principal principal) {
        if (teamService.findByAuthorities(team.getAuthorities()) != null) {
            throw new CustomException("هذة المجموعة موجودة بالفعل.");
        }
        Team topTeam = teamService.findTopByOrderByCodeDesc();
        if (topTeam == null) {
            team.setCode(1);
        } else {
            team.setCode(topTeam.getCode() + 1);
        }
        team = teamService.save(team);
        Person caller = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
        notificationService.notifyOne(Notification
                .builder()
                .title(lang.equals("AR") ? "العمليات على الصلاحيات" : "Data Processing")
                .message(lang.equals("AR") ? "تم انشاء مجموعة صلاحيات جديدة بنجاح" : "Create Privileges Successfully")
                .type("success")
                .icon("fa-plus-square")
                .layout(lang.equals("AR") ? "topLeft" : "topRight")
                .build(), principal.getName());
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), team);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_TEAM_UPDATE')")
    public String update(@RequestBody Team team, Principal principal) {
        if (teamService.findByCodeAndIdIsNot(team.getCode(), team.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Team object = teamService.findOne(team.getId());
        if (object != null) {
            team = teamService.save(team);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على الصلاحيات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم تعديل الصلاحيات بنجاح" : "Update Privileges Successfully")
                    .type("warning")
                    .icon("fa-edit")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), team);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_TEAM_DELETE')")
    public void delete(@PathVariable Long id, Principal principal) {
        Team team = teamService.findOne(id);
        if (team != null) {
            if (!team.getPersons().isEmpty()) {
                throw new CustomException("لا يمكن حذف هذة المجموعة لإعتماد بعض المستخدمين عليها.");
            }
            teamService.delete(id);
            Person caller = personService.findByEmail(principal.getName());
            String lang = JSONConverter.toObject(caller.getOptions(), Options.class).getLang();
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "العمليات على الصلاحيات" : "Data Processing")
                    .message(lang.equals("AR") ? "تم حذف مجموعة الصلاحيات بنجاح" : "Delete Privileges Successfully")
                    .type("error")
                    .icon("fa-trash")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAll() {
        List<Team> list = Lists.newArrayList(teamService.findAll());
        list.sort(Comparator.comparing(Team::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), list);
    }

    @RequestMapping(value = "findAllCombo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findAllCombo() {
        List<Team> list = Lists.newArrayList(teamService.findAll());
        list.sort(Comparator.comparing(Team::getCode));
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TEAM_COMBO), list);
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findOne(@PathVariable Long id) {
        return SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FILTER_TABLE), teamService.findOne(id));
    }
}
