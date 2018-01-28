package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.Falcon;
import com.besafx.app.entity.History;
import com.besafx.app.rest.FalconRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class FalconListener {

    private static final Logger log = LoggerFactory.getLogger(FalconListener.class);

    @PrePersist
    public void prePersist(Falcon falcon) {
        perform(falcon, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(Falcon falcon) {
        perform(falcon, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(Falcon falcon) {
        perform(falcon, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(Falcon falcon, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(falcon.getClass().getSimpleName());
            history.setScreenName(Falcon.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FalconRest.FILTER_FALCON_COMBO), falcon));
            history.setNote("رقم الصقر / " + falcon.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
