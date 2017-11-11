package com.besafx.app.service;

import com.besafx.app.entity.OrderDetectionType;
import com.besafx.app.entity.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public interface OrderDetectionTypeService extends PagingAndSortingRepository<OrderDetectionType, Long>, JpaSpecificationExecutor<OrderDetectionType> {
    List<OrderDetectionType> findByOrderId(Long orderId);
    List<OrderDetectionType> findByDoneIsTrue();
    List<OrderDetectionType> findByDoneIsFalse();
}
