package com.besafx.app.service;

import com.besafx.app.entity.BillBuy;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface BillBuyService extends PagingAndSortingRepository<BillBuy, Long>, JpaSpecificationExecutor<BillBuy> {
    BillBuy findTopByOrderByCodeDesc();

    BillBuy findByCodeAndIdIsNot(Integer code, Long id);
}
