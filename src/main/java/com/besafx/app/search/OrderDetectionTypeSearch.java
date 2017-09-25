package com.besafx.app.search;

import com.besafx.app.entity.Order;
import com.besafx.app.entity.OrderDetectionType;
import com.besafx.app.entity.comparator.OrderDetectionTypeComparator;
import com.besafx.app.entity.enums.OrderCondition;
import com.besafx.app.service.OrderDetectionTypeService;
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
public class OrderDetectionTypeSearch {

    private final Logger log = LoggerFactory.getLogger(OrderDetectionTypeSearch.class);

    @Autowired
    private OrderDetectionTypeService orderDetectionTypeService;

    public List<OrderDetectionType> filter(
            final Long codeFrom,
            final Long codeTo,
            final List<OrderCondition> orderConditions,
            final Long dateFrom,
            final Long dateTo,
            final String customerName,
            final String customerMobile,
            final String customerIdentityNumber,
            final Long falconCode,
            final String falconType,
            final Double weightFrom,
            final Double weightTo,
            final String doctorName
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(orderConditions)
                .ifPresent(value -> predicates.add((root, cq, cb) -> root.get("order").get("orderCondition").in(value)));
        Optional.ofNullable(codeFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("order").get("code"), value)));
        Optional.ofNullable(codeTo)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("order").get("code"), value)));
        Optional.ofNullable(dateFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("order").get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("order").get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(customerName)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("customer").get("name"), "%" + value + "%")));
        Optional.ofNullable(customerMobile)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("customer").get("mobile"), "%" + value + "%")));
        Optional.ofNullable(customerIdentityNumber)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("customer").get("identityNumber"), "%" + value + "%")));
        Optional.ofNullable(falconCode)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("code"), "%" + value + "%")));
        Optional.ofNullable(falconType)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("type"), "%" + value + "%")));
        Optional.ofNullable(weightFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("order").get("falcon").get("weight"), value)));
        Optional.ofNullable(weightTo)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("order").get("falcon").get("weight"), value)));
        Optional.ofNullable(doctorName)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("doctor").get("person").get("name"), "%" + value + "%")));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List<OrderDetectionType> list = orderDetectionTypeService.findAll(result);
            list.sort(Comparator.comparing(orderDetectionType -> orderDetectionType.getOrder().getCode()));
            return list;
        } else {
            return null;
        }

    }
}
