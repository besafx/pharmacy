package com.besafx.app.service;

import com.besafx.app.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface WithdrawService extends PagingAndSortingRepository<Withdraw, Long>, JpaSpecificationExecutor<Withdraw> {
    Withdraw findTopByOrderByCodeDesc();

    Withdraw findByCodeAndIdIsNot(Integer code, Long id);

    List<Withdraw> findByBankId(Long bankId);
}

