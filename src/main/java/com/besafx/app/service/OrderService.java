package com.besafx.app.service;

import com.besafx.app.entity.Falcon;
import com.besafx.app.entity.Order;
import org.joda.time.DateTime;
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
    List<Order> findByFalcon(Falcon falcon);
    Long countByDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
}
