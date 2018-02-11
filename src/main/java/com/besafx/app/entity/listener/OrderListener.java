package com.besafx.app.entity.listener;


import com.besafx.app.auditing.Action;
import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.History;
import com.besafx.app.entity.Order;
import com.besafx.app.rest.OrderRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class OrderListener {

    private static final Logger log = LoggerFactory.getLogger(OrderListener.class);

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.MANDATORY)
    public void perform(Order order, Action action, String note) {
        try {
            EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
            History history = new History();
            history.setClassName(order.getClass().getSimpleName());
            history.setScreenName(Order.SCREEN_NAME);
            history.setAction(action);
            history.setObjectJson(SquigglyUtils.stringify(Squiggly.init(new ObjectMapper(), OrderRest.FILTER_TABLE_INFO), order));
            history.setNote(note);
            entityManager.persist(history);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
