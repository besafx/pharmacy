package com.besafx.app.service;

import com.besafx.app.entity.Deposit;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface DepositService extends PagingAndSortingRepository<Deposit, Long>, JpaSpecificationExecutor<Deposit> {
    Deposit findTopByOrderByCodeDesc();

    Deposit findByCodeAndIdIsNot(Integer code, Long id);

    List<Deposit> findByBankId(Long bankId);
}

