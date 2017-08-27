package com.besafx.app.service;

import com.besafx.app.entity.Employee;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface EmployeeService extends PagingAndSortingRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Employee findTopByOrderByCodeDesc();
    Employee findByCodeAndIdIsNot(Integer code, Long id);
}

