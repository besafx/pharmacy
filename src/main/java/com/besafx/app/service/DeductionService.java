package com.besafx.app.service;

import com.besafx.app.entity.Deduction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface DeductionService extends PagingAndSortingRepository<Deduction, Long>, JpaSpecificationExecutor<Deduction> {
}
