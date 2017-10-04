package com.besafx.app.service;

import com.besafx.app.entity.BillSell;
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
public interface BillSellService extends PagingAndSortingRepository<BillSell, Long>, JpaSpecificationExecutor<BillSell> {
    BillSell findTopByOrderByCodeDesc();
    BillSell findByCodeAndIdIsNot(Integer code, Long id);
    List<BillSell> findByIdIn(List<Long> ids);
    List<BillSell> findByDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
    BillSell findByOrder(Order order);
}
