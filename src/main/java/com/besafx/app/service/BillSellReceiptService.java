package com.besafx.app.service;

import com.besafx.app.entity.BillSellReceipt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface BillSellReceiptService extends PagingAndSortingRepository<BillSellReceipt, Long>, JpaSpecificationExecutor<BillSellReceipt> {
    List<BillSellReceipt> findByBillSellId(Long id);
}

