package com.besafx.app.service;

import com.besafx.app.entity.Drug;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface DrugService extends PagingAndSortingRepository<Drug, Long>, JpaSpecificationExecutor<Drug> {
    Drug findTopByOrderByCodeDesc();

    Drug findByCode(Integer code);

    Drug findByCodeAndIdIsNot(Integer code, Long id);

    List<Drug> findByIdIn(List<Long> ids);
}
