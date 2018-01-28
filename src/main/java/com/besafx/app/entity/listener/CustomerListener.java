package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.Customer;
import com.besafx.app.rest.CustomerRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

public class CustomerListener {

    private static final Logger log = LoggerFactory.getLogger(CustomerListener.class);

    @PrePersist
    public void prePersist(Customer customer) {
        perform(customer, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(Customer customer) {
        perform(customer, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(Customer customer) {
        perform(customer, Action.DELETED);
    }

    @Transactional(Transactional.TxType.MANDATORY)
    private void perform(Customer customer, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(customer.getClass().getSimpleName());
            history.setScreenName(Customer.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), CustomerRest.FILTER_CUSTOMER_INFO), customer));
            history.setNote("رقم العميل / " + customer.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
