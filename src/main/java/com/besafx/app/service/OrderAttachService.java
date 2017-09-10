package com.besafx.app.service;

import com.besafx.app.entity.Order;
import com.besafx.app.entity.OrderAttach;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderAttachService extends PagingAndSortingRepository<OrderAttach, Long>, JpaSpecificationExecutor<OrderAttach> {
    List<OrderAttach> findByOrder(Order id);
    List<OrderAttach> findByOrderIn(List<Order> orders);
    List<OrderAttach> findByOrderId(Long id);
    List<OrderAttach> findByOrderIdIn(List<Long> orderIds);
}
