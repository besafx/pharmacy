package com.besafx.app.service;

import com.besafx.app.entity.FundReceipt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface FundReceiptService extends PagingAndSortingRepository<FundReceipt, Long>, JpaSpecificationExecutor<FundReceipt> {
    List<FundReceipt> findByFundId(Long id);
}

