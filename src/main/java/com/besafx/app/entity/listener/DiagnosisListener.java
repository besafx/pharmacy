package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.Diagnosis;
import com.besafx.app.rest.DiagnosisRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class DiagnosisListener {

    private static final Logger log = LoggerFactory.getLogger(DiagnosisListener.class);

    @PrePersist
    public void prePersist(Diagnosis diagnosis) {
        perform(diagnosis, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(Diagnosis diagnosis) {
        perform(diagnosis, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(Diagnosis diagnosis) {
        perform(diagnosis, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(Diagnosis diagnosis, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(diagnosis.getClass().getSimpleName());
            history.setScreenName(Diagnosis.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), DiagnosisRest.FILTER_TABLE), diagnosis));
            history.setNote("رقم التشخيص / " + diagnosis.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
