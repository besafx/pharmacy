package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.DrugUnit;
import com.besafx.app.rest.DrugUnitRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class DrugUnitListener {

    private static final Logger log = LoggerFactory.getLogger(DrugUnitListener.class);

    @PrePersist
    public void prePersist(DrugUnit drugUnit) {
        perform(drugUnit, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(DrugUnit drugUnit) {
        perform(drugUnit, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(DrugUnit drugUnit) {
        perform(drugUnit, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(DrugUnit drugUnit, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(drugUnit.getClass().getSimpleName());
            history.setScreenName(DrugUnit.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), DrugUnitRest.FILTER_TABLE), drugUnit));
            history.setNote("رقم الوحدة / " + drugUnit.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
