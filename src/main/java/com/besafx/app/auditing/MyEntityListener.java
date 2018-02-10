package com.besafx.app.auditing;


import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Component
public class MyEntityListener {

    private static final Logger log = LoggerFactory.getLogger(MyEntityListener.class);

    @PrePersist
    public void prePersist(Object object) {
        perform(object, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(Object object) {
        perform(object, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(Object object) {
        perform(object, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(Object object, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(object.getClass().getSimpleName());
            history.setScreenName((String) object.getClass().getField("SCREEN_NAME").get(object));
            history.setAction(action);
            String baseRestPackageName = "com.besafx.app.rest.";
            Class restClass = Class.forName(baseRestPackageName + object.getClass().getSimpleName().concat("Rest"));
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), (String) restClass.getField("FILTER_TABLE").get(object)), object));
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
