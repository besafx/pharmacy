package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.Team;
import com.besafx.app.rest.TeamRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class TeamListener {

    private static final Logger log = LoggerFactory.getLogger(TeamListener.class);

    @PrePersist
    public void prePersist(Team team) {
        perform(team, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(Team team) {
        perform(team, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(Team team) {
        perform(team, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(Team team, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(team.getClass().getSimpleName());
            history.setScreenName(Team.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), TeamRest.FILTER_TEAM_COMBO), team));
            history.setNote("رقم المجموعة / " + team.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
