package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.TransactionBuy;
import com.besafx.app.entity.History;
import com.besafx.app.rest.TransactionBuyRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class TransactionBuyListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionBuyListener.class);

    @PrePersist
    public void prePersist(TransactionBuy transactionBuy) {
        perform(transactionBuy, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(TransactionBuy transactionBuy) {
        perform(transactionBuy, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(TransactionBuy transactionBuy) {
        perform(transactionBuy, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(TransactionBuy transactionBuy, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(transactionBuy.getClass().getSimpleName());
            history.setScreenName(TransactionBuy.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), TransactionBuyRest.FILTER_TABLE), transactionBuy));
            history.setNote("رقم حركة الشراء / " + transactionBuy.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
