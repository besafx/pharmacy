package com.besafx.app.service;

import com.besafx.app.entity.DetectionType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface DetectionTypeService extends PagingAndSortingRepository<DetectionType, Long>, JpaSpecificationExecutor<DetectionType> {
    DetectionType findTopByOrderByCodeDesc();

    DetectionType findByCode(Integer code);

    DetectionType findByCodeAndIdIsNot(Integer code, Long id);
}
