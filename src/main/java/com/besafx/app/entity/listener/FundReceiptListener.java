package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.FundReceipt;
import com.besafx.app.entity.History;
import com.besafx.app.rest.FundReceiptRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class FundReceiptListener {

    private static final Logger log = LoggerFactory.getLogger(FundReceiptListener.class);

    @PrePersist
    public void prePersist(FundReceipt fundReceipt) {
        perform(fundReceipt, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(FundReceipt fundReceipt) {
        perform(fundReceipt, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(FundReceipt fundReceipt) {
        perform(fundReceipt, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(FundReceipt fundReceipt, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(fundReceipt.getClass().getSimpleName());
            history.setScreenName(FundReceipt.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), FundReceiptRest.FILTER_TABLE), fundReceipt));
            history.setNote("رقم السند / " + fundReceipt.getReceipt().getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
