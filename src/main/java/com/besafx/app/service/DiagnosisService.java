package com.besafx.app.service;

import com.besafx.app.entity.Diagnosis;
import com.besafx.app.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface DiagnosisService extends PagingAndSortingRepository<Diagnosis, Long>, JpaSpecificationExecutor<Diagnosis> {
    Diagnosis findByCode(Long code);

    Diagnosis findTopByOrderByCodeDesc();

    Diagnosis findByCodeAndIdIsNot(Long code, Long id);

    List<Diagnosis> findByOrder(Order order);

    List<Diagnosis> findByOrderId(Long order);
}
