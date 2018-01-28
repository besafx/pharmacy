package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.BillSellReceipt;
import com.besafx.app.entity.History;
import com.besafx.app.rest.BillSellReceiptRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class BillSellReceiptListener {

    private static final Logger log = LoggerFactory.getLogger(BillSellReceiptListener.class);

    @PrePersist
    public void prePersist(BillSellReceipt billSellReceipt) {
        perform(billSellReceipt, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(BillSellReceipt billSellReceipt) {
        perform(billSellReceipt, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(BillSellReceipt billSellReceipt) {
        perform(billSellReceipt, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    private void perform(BillSellReceipt billSellReceipt, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(billSellReceipt.getClass().getSimpleName());
            history.setScreenName(BillSellReceipt.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), BillSellReceiptRest.FILTER_TABLE), billSellReceipt));
            history.setNote("رقم السند / " + billSellReceipt.getReceipt().getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
