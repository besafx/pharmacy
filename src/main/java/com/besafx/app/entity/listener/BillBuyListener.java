package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.BillBuy;
import com.besafx.app.entity.BillBuy;
import com.besafx.app.entity.History;
import com.besafx.app.rest.BillBuyRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class BillBuyListener {

    private static final Logger log = LoggerFactory.getLogger(BillBuyListener.class);

    @PrePersist
    public void prePersist(BillBuy billBuy) {
        perform(billBuy, Action.INSERTED);
    }

    @PreUpdate
    public void preUpdate(BillBuy billBuy) {
        perform(billBuy, Action.UPDATED);
    }

    @PreRemove
    public void preRemove(BillBuy billBuy) {
        perform(billBuy, Action.DELETED);
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(BillBuy billBuy, Action action) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(billBuy.getClass().getSimpleName());
            history.setScreenName(BillBuy.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), BillBuyRest.FILTER_BILL_BUY_COMBO), billBuy));
            history.setNote("رقم فاتورة الشراء / " + billBuy.getCode());
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
