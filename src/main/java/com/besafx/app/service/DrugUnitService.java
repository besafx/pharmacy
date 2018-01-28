package com.besafx.app.service;

import com.besafx.app.entity.Drug;
import com.besafx.app.entity.DrugUnit;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface DrugUnitService extends PagingAndSortingRepository<DrugUnit, Long>, JpaSpecificationExecutor<DrugUnit> {
    DrugUnit findTopByAndCodeIsNotNullOrderByCodeDesc();

    DrugUnit findTopByDrugOrderByFactorAsc(Drug drug);

    DrugUnit findByCodeAndIdIsNot(Integer code, Long id);

    List<DrugUnit> findByDrugUnitId(Long id);

    List<DrugUnit> findByDrugId(Long id);

    DrugUnit findByNameAndFactorAndDrug(String name, Integer factor, Drug drug);
}
