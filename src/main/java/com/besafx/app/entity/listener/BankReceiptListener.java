package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.BankReceipt;
import com.besafx.app.entity.History;
import com.besafx.app.rest.BankReceiptRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class BankReceiptListener {

    private static final Logger log = LoggerFactory.getLogger(BankReceiptListener.class);

    @PrePersist
    public void prePersist(BankReceipt bankReceipt) {
        perform(bankReceipt, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(BankReceipt bankReceipt) {
        perform(bankReceipt, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(BankReceipt bankReceipt) {
        perform(bankReceipt, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(BankReceipt bankReceipt, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(bankReceipt.getClass().getSimpleName());
            history.setScreenName(BankReceipt.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), BankReceiptRest.FILTER_TABLE), bankReceipt));
            history.setNote("رقم السند / " + bankReceipt.getReceipt().getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
