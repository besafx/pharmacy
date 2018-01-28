package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.Person;
import com.besafx.app.rest.PersonRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class PersonListener {

    private static final Logger log = LoggerFactory.getLogger(PersonListener.class);

    @PrePersist
    public void prePersist(Person person) {
        perform(person, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(Person person) {
        perform(person, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(Person person) {
        perform(person, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(Person person, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(person.getClass().getSimpleName());
            history.setScreenName(Person.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), PersonRest.FILTER_TABLE), person));
            history.setNote("مالك الحساب / " + person.getName());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
