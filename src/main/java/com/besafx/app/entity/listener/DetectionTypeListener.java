package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.DetectionType;
import com.besafx.app.rest.DetectionTypeRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class DetectionTypeListener {

    private static final Logger log = LoggerFactory.getLogger(DetectionTypeListener.class);

    @PrePersist
    public void prePersist(DetectionType detectionType) {
        perform(detectionType, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(DetectionType detectionType) {
        perform(detectionType, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(DetectionType detectionType) {
        perform(detectionType, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(DetectionType detectionType, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(detectionType.getClass().getSimpleName());
            history.setScreenName(DetectionType.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), DetectionTypeRest.FILTER_TABLE), detectionType));
            history.setNote("رقم خدمة الفحص / " + detectionType.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
