package com.besafx.app.service;

import com.besafx.app.entity.BillBuyReceipt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface BillBuyReceiptService extends PagingAndSortingRepository<BillBuyReceipt, Long>, JpaSpecificationExecutor<BillBuyReceipt> {
    List<BillBuyReceipt> findByBillBuyId(Long id);
}

