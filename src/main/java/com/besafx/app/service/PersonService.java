package com.besafx.app.service;

import com.besafx.app.entity.Person;
import com.besafx.app.entity.enums.PersonType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface PersonService extends PagingAndSortingRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    Person findByEmail(String email);
    List<Person> findByPersonType(PersonType personType);
}

