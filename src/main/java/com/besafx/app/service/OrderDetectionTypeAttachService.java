package com.besafx.app.service;

import com.besafx.app.entity.OrderDetectionType;
import com.besafx.app.entity.OrderDetectionTypeAttach;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderDetectionTypeAttachService extends PagingAndSortingRepository<OrderDetectionTypeAttach, Long>, JpaSpecificationExecutor<OrderDetectionTypeAttach> {
    List<OrderDetectionTypeAttach> findByOrderDetectionType(OrderDetectionType id);
    List<OrderDetectionTypeAttach> findByOrderDetectionTypeIn(List<OrderDetectionType> orders);
    List<OrderDetectionTypeAttach> findByOrderDetectionTypeId(Long id);
    List<OrderDetectionTypeAttach> findByOrderDetectionTypeIdIn(List<Long> orderIds);
}
