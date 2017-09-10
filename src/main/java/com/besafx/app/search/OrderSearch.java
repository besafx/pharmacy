package com.besafx.app.search;

import com.besafx.app.entity.Order;
import com.besafx.app.entity.enums.OrderCondition;
import com.besafx.app.service.OrderService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderSearch {

    private final Logger log = LoggerFactory.getLogger(OrderSearch.class);

    @Autowired
    private OrderService orderService;

    public List<Order> filter(
            final Long codeFrom,
            final Long codeTo,
            final List<OrderCondition> orderConditions,
            final Long dateFrom,
            final Long dateTo,
            final List<Long> falcons,
            final List<Long> doctors
    ) {

        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(orderConditions).ifPresent(value -> predicates.add((root, cq, cb) -> root.get("orderCondition").in(value)));
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(falcons).ifPresent(value -> predicates.add((root, cq, cb) -> root.get("falcon").get("id").in(value)));
        Optional.ofNullable(doctors).ifPresent(value -> predicates.add((root, cq, cb) -> root.get("doctor").get("id").in(value)));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            return orderService.findAll(result);
        } else {
            return null;
        }

    }
}
