package com.besafx.app.search;

import com.besafx.app.entity.Falcon;
import com.besafx.app.entity.Order;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.service.FalconService;
import com.besafx.app.service.OrderService;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
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
public class FalconSearch {

    private final Logger log = LoggerFactory.getLogger(FalconSearch.class);

    @Autowired
    private FalconService falconService;

    public List<Falcon> filter(
            final String customerName,
            final String customerMobile,
            final String customerIdentityNumber,
            final Long falconCode,
            final String falconType,
            final Double weightFrom,
            final Double weightTo
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(customerName)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("customer").get("name"), "%" + value + "%")));
        Optional.ofNullable(customerMobile)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("customer").get("mobile"), "%" + value + "%")));
        Optional.ofNullable(customerIdentityNumber)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("customer").get("identityNumber"), "%" + value + "%")));
        Optional.ofNullable(falconCode)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("code"), "%" + value + "%")));
        Optional.ofNullable(falconType)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("type"), "%" + value + "%")));
        Optional.ofNullable(weightFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("weight"), value)));
        Optional.ofNullable(weightTo)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("weight"), value)));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List list = falconService.findAll(result);
            list.sort(Comparator.comparing(Falcon::getCode).reversed());
            return list;
        } else {
            List<Falcon> list = Lists.newArrayList(falconService.findAll());
            list.sort(Comparator.comparing(Falcon::getCode).reversed());
            return list;
        }
    }
}
