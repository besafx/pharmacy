package com.besafx.app.service;
import com.besafx.app.entity.DrugUnit;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface DrugUnitService extends PagingAndSortingRepository<DrugUnit, Long>, JpaSpecificationExecutor<DrugUnit> {
    List<DrugUnit> findByDrugUnitId(Long id);
}
