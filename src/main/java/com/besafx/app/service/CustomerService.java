package com.besafx.app.service;

import com.besafx.app.entity.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface CustomerService extends PagingAndSortingRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Customer findTopByOrderByCodeDesc();

    Customer findByCodeAndIdIsNot(Integer code, Long id);
}

