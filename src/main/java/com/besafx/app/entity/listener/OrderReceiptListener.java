package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.OrderReceipt;
import com.besafx.app.entity.History;
import com.besafx.app.rest.OrderReceiptRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class OrderReceiptListener {

    private static final Logger log = LoggerFactory.getLogger(OrderReceiptListener.class);

    @PrePersist
    public void prePersist(OrderReceipt orderReceipt) {
        perform(orderReceipt, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(OrderReceipt orderReceipt) {
        perform(orderReceipt, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(OrderReceipt orderReceipt) {
        perform(orderReceipt, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(OrderReceipt orderReceipt, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(orderReceipt.getClass().getSimpleName());
            history.setScreenName(OrderReceipt.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), OrderReceiptRest.FILTER_TABLE), orderReceipt));
            history.setNote("رقم السند / " + orderReceipt.getReceipt().getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
