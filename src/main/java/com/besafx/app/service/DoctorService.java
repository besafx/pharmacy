package com.besafx.app.service;

import com.besafx.app.entity.Doctor;
import com.besafx.app.entity.Person;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface DoctorService extends PagingAndSortingRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    Doctor findTopByOrderByCodeDesc();

    Doctor findByCodeAndIdIsNot(Integer code, Long id);

    Doctor findByPerson(Person person);
}

