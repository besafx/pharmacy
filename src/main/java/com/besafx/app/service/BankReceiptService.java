package com.besafx.app.service;

import com.besafx.app.entity.BankReceipt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface BankReceiptService extends PagingAndSortingRepository<BankReceipt, Long>, JpaSpecificationExecutor<BankReceipt> {
    List<BankReceipt> findByBankId(Long id);
}

