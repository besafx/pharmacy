package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.Supplier;
import com.besafx.app.rest.SupplierRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class SupplierListener {

    private static final Logger log = LoggerFactory.getLogger(SupplierListener.class);

    @PrePersist
    public void prePersist(Supplier supplier) {
        perform(supplier, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(Supplier supplier) {
        perform(supplier, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(Supplier supplier) {
        perform(supplier, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(Supplier supplier, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(supplier.getClass().getSimpleName());
            history.setScreenName(Supplier.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), SupplierRest.FILTER_SUPPLIER_COMBO), supplier));
            history.setNote("رقم المورد / " + supplier.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
