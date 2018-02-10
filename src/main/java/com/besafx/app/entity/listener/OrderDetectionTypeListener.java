package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.OrderDetectionType;
import com.besafx.app.rest.OrderDetectionTypeRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class OrderDetectionTypeListener {

    private static final Logger log = LoggerFactory.getLogger(OrderDetectionTypeListener.class);

    @PrePersist
    public void prePersist(OrderDetectionType orderDetectionType) {
        perform(orderDetectionType, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(OrderDetectionType orderDetectionType) {
        perform(orderDetectionType, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(OrderDetectionType orderDetectionType) {
        perform(orderDetectionType, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(OrderDetectionType orderDetectionType, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(orderDetectionType.getClass().getSimpleName());
            history.setScreenName(OrderDetectionType.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), OrderDetectionTypeRest.FILTER_TABLE), orderDetectionType));
            history.setNote("رقم خدمة الفحص / " + orderDetectionType.getDetectionType().getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
