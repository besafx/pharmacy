package com.besafx.app.search;

import com.besafx.app.entity.Order;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.service.OrderService;
import com.besafx.app.util.DateConverter;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderSearch {

    private final Logger log = LoggerFactory.getLogger(OrderSearch.class);

    @Autowired
    private OrderService orderService;

    public List<Order> filter(
            final Long codeFrom,
            final Long codeTo,
            final List<PaymentMethod> paymentMethods,
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
        Optional.ofNullable(codeFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(paymentMethods)
                .ifPresent(value -> predicates.add((root, cq, cb) -> root.get("paymentMethod").in(value)));
        Optional.ofNullable(dateFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(customerName)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("falcon").get("customer").get("name"), "%" + value + "%")));
        Optional.ofNullable(customerMobile)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("falcon").get("customer").get("mobile"), "%" + value + "%")));
        Optional.ofNullable(customerIdentityNumber)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("falcon").get("customer").get("identityNumber"), "%" + value + "%")));
        Optional.ofNullable(falconCode)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("falcon").get("code").as(String.class), "%" + value + "%")));
        Optional.ofNullable(falconType)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("falcon").get("type"), "%" + value + "%")));
        Optional.ofNullable(weightFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("falcon").get("weight"), value)));
        Optional.ofNullable(weightTo)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("falcon").get("weight"), value)));
        Optional.ofNullable(doctorName)
                .ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("doctor").get("person").get("name"), "%" + value + "%")));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List list = orderService.findAll(result);
            list.sort(Comparator.comparing(Order::getCode).reversed());
            return list;
        } else {
            List<Order> list = Lists.newArrayList(orderService.findAll());
            list.sort(Comparator.comparing(Order::getCode).reversed());
            return list;
        }
    }

    public List<Order> findByToday() {
        List<Specification> predicates = new ArrayList<>();
        DateTime today = new DateTime().withTimeAtStartOfDay();
        DateTime tomorrow = new DateTime().plusDays(1).withTimeAtStartOfDay();
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), today.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), tomorrow.toDate()));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = orderService.findAll(result);
        list.sort(Comparator.comparing(Order::getCode).reversed());
        return list;
    }

    public List<Order> findByWeek() {
        List<Specification> predicates = new ArrayList<>();
        Date weekStart = DateConverter.getCurrentWeekStart();
        Date weekEnd = DateConverter.getCurrentWeekEnd();
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), weekStart));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), weekEnd));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = orderService.findAll(result);
        list.sort(Comparator.comparing(Order::getCode).reversed());
        return list;
    }

    public List<Order> findByMonth() {
        List<Specification> predicates = new ArrayList<>();
        DateTime monthStart = new DateTime().withTimeAtStartOfDay().withDayOfMonth(1);
        DateTime monthEnd = monthStart.plusMonths(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), monthStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), monthEnd.toDate()));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = orderService.findAll(result);
        list.sort(Comparator.comparing(Order::getCode).reversed());
        return list;
    }

    public List<Order> findByYear() {
        List<Specification> predicates = new ArrayList<>();
        DateTime yearStart = new DateTime().withTimeAtStartOfDay().withDayOfYear(1);
        DateTime yearEnd = yearStart.plusYears(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), yearStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), yearEnd.toDate()));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = orderService.findAll(result);
        list.sort(Comparator.comparing(Order::getCode).reversed());
        return list;
    }
}
