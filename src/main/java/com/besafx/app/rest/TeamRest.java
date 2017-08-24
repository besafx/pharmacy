package com.besafx.app.rest;

import com.besafx.app.config.CustomException;
import com.besafx.app.entity.Team;
import com.besafx.app.service.TeamService;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
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

    @Autowired
    private TeamService teamService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_TEAM_CREATE')")
    public Team create(@RequestBody Team team, Principal principal) {
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
        notificationService.notifyOne(Notification
                .builder()
                .title("انشاء بيانات")
                .message("تم انشاء مجموعة صلاحيات جديدة بنجاح")
                .type("success")
                .icon("fa-plus-circle")
                .build(), principal.getName());
        return team;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_TEAM_UPDATE')")
    public Team update(@RequestBody Team team, Principal principal) {
        if (teamService.findByCodeAndIdIsNot(team.getCode(), team.getId()) != null) {
            throw new CustomException("هذا الكود مستخدم سابقاً، فضلاً قم بتغير الكود.");
        }
        Team object = teamService.findOne(team.getId());
        if (object != null) {
            team = teamService.save(team);
            notificationService.notifyOne(Notification
                    .builder()
                    .title("تعديل بيانات")
                    .message("تم تعديل بيانات المجموعة بنجاح")
                    .type("success")
                    .icon("fa-edit")
                    .build(), principal.getName());
            return team;
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
            notificationService.notifyOne(Notification
                    .builder()
                    .title("حذف بيانات")
                    .message("تم حذف مجموعة الصلاحيات بنجاح")
                    .type("error")
                    .icon("fa-ban")
                    .build(), principal.getName());
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Team> findAll() {
        List<Team> list = Lists.newArrayList(teamService.findAll());
        list.sort(Comparator.comparing(Team::getCode));
        return list;
    }

    @RequestMapping(value = "findAllSummery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Team> findAllSummery() {
        return findAll();
    }

    @RequestMapping(value = "findOne/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Team findOne(@PathVariable Long id) {
        return teamService.findOne(id);
    }
}
