package com.besafx.app.service;

import com.besafx.app.entity.Order;
import com.besafx.app.entity.enums.OrderCondition;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderService extends PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Order findTopByOrderByCodeDesc();
    Order findByCodeAndIdIsNot(Integer code, Long id);

    List<Order> findByOrderConditionIn(List<OrderCondition> orderConditions);
}
