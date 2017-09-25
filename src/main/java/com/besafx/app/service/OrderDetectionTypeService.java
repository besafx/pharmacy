package com.besafx.app.service;

import com.besafx.app.entity.OrderDetectionType;
import com.besafx.app.entity.enums.OrderCondition;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderDetectionTypeService extends PagingAndSortingRepository<OrderDetectionType, Long>, JpaSpecificationExecutor<OrderDetectionType> {
    List<OrderDetectionType> findByOrderId(Long orderId);
    List<OrderDetectionType> findByOrderOrderConditionIn(List<OrderCondition> orderConditions);
}
