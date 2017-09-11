package com.besafx.app.service;

import com.besafx.app.entity.BillSellDetection;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface BillSellDetectionService extends PagingAndSortingRepository<BillSellDetection, Long>, JpaSpecificationExecutor<BillSellDetection> {
    BillSellDetection findTopByOrderByCodeDesc();
    BillSellDetection findByCodeAndIdIsNot(Integer code, Long id);
}
