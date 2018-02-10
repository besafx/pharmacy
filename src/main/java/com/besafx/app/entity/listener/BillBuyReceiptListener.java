package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.BillBuyReceipt;
import com.besafx.app.entity.History;
import com.besafx.app.rest.BillBuyReceiptRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class BillBuyReceiptListener {

    private static final Logger log = LoggerFactory.getLogger(BillBuyReceiptListener.class);

    @PrePersist
    public void prePersist(BillBuyReceipt billBuyReceipt) {
        perform(billBuyReceipt, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(BillBuyReceipt billBuyReceipt) {
        perform(billBuyReceipt, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(BillBuyReceipt billBuyReceipt) {
        perform(billBuyReceipt, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(BillBuyReceipt billBuyReceipt, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(billBuyReceipt.getClass().getSimpleName());
            history.setScreenName(BillBuyReceipt.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), BillBuyReceiptRest.FILTER_TABLE), billBuyReceipt));
            history.setNote("رقم السند / " + billBuyReceipt.getReceipt().getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
