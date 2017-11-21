package com.besafx.app.search;

import com.besafx.app.entity.Customer;
import com.besafx.app.entity.Falcon;
import com.besafx.app.service.CustomerService;
import com.besafx.app.service.FalconService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class CustomerSearch {

    private final Logger log = LoggerFactory.getLogger(CustomerSearch.class);

    @Autowired
    private CustomerService customerService;

    public List<Customer> filter(
            final String code,
            final String name,
            final String mobile,
            final String identityNumber,
            final String email
    ) {
        List<Specification> predicates = new ArrayList<>();

        Optional.ofNullable(code)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("code").as(String.class), "%" + value + "%")));
        Optional.ofNullable(name)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("name"), "%" + value + "%")));
        Optional.ofNullable(mobile)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("mobile"), "%" + value + "%")));
        Optional.ofNullable(identityNumber)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("identityNumber"), "%" + value + "%")));
        Optional.ofNullable(email)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("email"), "%" + value + "%")));

        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List list = customerService.findAll(result);
            list.sort(Comparator.comparing(Customer::getCode));
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
