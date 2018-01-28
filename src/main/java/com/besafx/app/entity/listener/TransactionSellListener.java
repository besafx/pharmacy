package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.TransactionSell;
import com.besafx.app.rest.TransactionSellRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class TransactionSellListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionSellListener.class);

    @PrePersist
    public void prePersist(TransactionSell transactionSell) {
        perform(transactionSell, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(TransactionSell transactionSell) {
        perform(transactionSell, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(TransactionSell transactionSell) {
        perform(transactionSell, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(TransactionSell transactionSell, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(transactionSell.getClass().getSimpleName());
            history.setScreenName(TransactionSell.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), TransactionSellRest.FILTER_TABLE), transactionSell));
            history.setNote("رقم حركة البيع / " + transactionSell.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
