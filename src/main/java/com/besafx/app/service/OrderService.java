package com.besafx.app.service;

import com.besafx.app.entity.Falcon;
import com.besafx.app.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public interface OrderService extends PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Order findTopByOrderByCodeDesc();

    Order findByCodeAndIdIsNot(Integer code, Long id);

    List<Order> findByIdIn(List<Long> ids);

    List<Order> findByDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);

    List<Order> findByFalconIn(List<Falcon> falcons);

    List<Order> findByFalconIdIn(List<Long> falcons);

    List<Order> findByFalcon(Falcon falcon);

    List<Order> findByFalconId(Long falconId);

    List<Order> findByFalconIdAndCodeNot(Long falconId, Integer code);

    List<Order> findByFalconCustomerId(Long customerId);

    List<Order> findByFalconCustomerIdAndCodeNot(Long customerId, Integer code);

    Long countByDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
}
