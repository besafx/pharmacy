package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.Drug;
import com.besafx.app.rest.DrugRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class DrugListener {

    private static final Logger log = LoggerFactory.getLogger(DrugListener.class);

    @PrePersist
    public void prePersist(Drug drug) {
        perform(drug, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(Drug drug) {
        perform(drug, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(Drug drug) {
        perform(drug, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(Drug drug, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(drug.getClass().getSimpleName());
            history.setScreenName(Drug.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), DrugRest.FILTER_DRUG_COMBO), drug));
            history.setNote("رقم الصنف / " + drug.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
