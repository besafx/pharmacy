package com.besafx.app.service;

import com.besafx.app.entity.OrderReceipt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderReceiptService extends PagingAndSortingRepository<OrderReceipt, Long>, JpaSpecificationExecutor<OrderReceipt> {
    List<OrderReceipt> findByOrderId(Long id);
}

