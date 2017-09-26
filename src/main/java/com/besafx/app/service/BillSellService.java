package com.besafx.app.service;

import com.besafx.app.entity.BillSell;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface BillSellService extends PagingAndSortingRepository<BillSell, Long>, JpaSpecificationExecutor<BillSell> {
    BillSell findTopByOrderByCodeDesc();
    BillSell findByCodeAndIdIsNot(Integer code, Long id);
}
