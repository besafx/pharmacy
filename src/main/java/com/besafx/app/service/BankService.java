package com.besafx.app.service;

import com.besafx.app.entity.Bank;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface BankService extends PagingAndSortingRepository<Bank, Long>, JpaSpecificationExecutor<Bank> {
    Bank findTopByOrderByCodeDesc();

    Bank findByCodeAndIdIsNot(Integer code, Long id);
}

