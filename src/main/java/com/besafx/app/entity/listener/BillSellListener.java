package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.BillSell;
import com.besafx.app.entity.History;
import com.besafx.app.rest.BillSellRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class BillSellListener {

    private static final Logger log = LoggerFactory.getLogger(BillSellListener.class);

    @PrePersist
    public void prePersist(BillSell billSell) {
        perform(billSell, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(BillSell billSell) {
        perform(billSell, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(BillSell billSell) {
        perform(billSell, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(BillSell billSell, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(billSell.getClass().getSimpleName());
            history.setScreenName(BillSell.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), BillSellRest.FILTER_BILL_SELL_COMBO), billSell));
            history.setNote("رقم فاتورة البيع / " + billSell.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
