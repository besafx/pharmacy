package com.besafx.app.service;

import com.besafx.app.entity.DrugCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface DrugCategoryService extends PagingAndSortingRepository<DrugCategory, Long>, JpaSpecificationExecutor<DrugCategory> {
    DrugCategory findTopByOrderByCodeDesc();
    DrugCategory findByCodeAndIdIsNot(Integer code, Long id);
}
