package com.besafx.app.service;

import com.besafx.app.entity.TransactionSell;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface TransactionSellService extends PagingAndSortingRepository<TransactionSell, Long>, JpaSpecificationExecutor<TransactionSell> {
    TransactionSell findTopByOrderByCodeDesc();
    TransactionSell findByCodeAndIdIsNot(Integer code, Long id);
}
