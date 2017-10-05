package com.besafx.app.service;

import com.besafx.app.entity.Drug;
import com.besafx.app.entity.Order;
import com.besafx.app.entity.OrderDetectionType;
import com.besafx.app.entity.TransactionSell;
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
public interface TransactionSellService extends PagingAndSortingRepository<TransactionSell, Long>, JpaSpecificationExecutor<TransactionSell> {
    TransactionSell findTopByOrderByCodeDesc();
    TransactionSell findByCodeAndIdIsNot(Integer code, Long id);
    Long countByBillSellOrderAndTransactionBuyDrug(Order order, Drug drug);
    List<TransactionSell> findByDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
}
