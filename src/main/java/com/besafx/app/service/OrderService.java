package com.besafx.app.service;
import com.besafx.app.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface OrderService extends PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Order findTopByOrderByCodeDesc();
    Order findByCodeAndIdIsNot(Integer code, Long id);
}
