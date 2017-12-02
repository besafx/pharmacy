package com.besafx.app.search;

import com.besafx.app.entity.BillSell;
import com.besafx.app.entity.BillSell;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.service.BillSellService;
import com.besafx.app.util.DateConverter;
import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BillSellSearch {

    private final Logger log = LoggerFactory.getLogger(BillSellSearch.class);

    @Autowired
    private BillSellService billSellService;

    public List<BillSell> filter(
            final Long codeFrom,
            final Long codeTo,
            final Boolean viewInsideSalesTable,
            final List<PaymentMethod> paymentMethods,
            final String checkCode,
            final Long dateFrom,
            final Long dateTo,
            final Long orderCodeFrom,
            final Long orderCodeTo,
            final String orderFalconCode,
            final String orderCustomerName
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(paymentMethods).ifPresent(value -> predicates.add((root, cq, cb) -> root.get("paymentMethod").in(value)));
        Optional.ofNullable(checkCode).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("checkCode"), "%" + value + "%")));
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(orderCodeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("order").get("code"), value)));
        Optional.ofNullable(orderCodeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("order").get("code"), value)));

        Optional.ofNullable(viewInsideSalesTable).ifPresent(value -> predicates.add(value ? (root, cq, cb) -> cb.isNotNull(root.get("order")) : (root, cq, cb) -> cb.isNull(root.get("order"))));
        Optional.ofNullable(viewInsideSalesTable)
                .ifPresent(value -> {
                    if(value){
                        Optional.ofNullable(orderFalconCode).ifPresent(code -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("code"), "%" + code + "%")));
                        Optional.ofNullable(orderCustomerName).ifPresent(name -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("customer").get("name"), "%" + name + "%")));
                    }else{
                        Optional.ofNullable(orderFalconCode).ifPresent(code -> predicates.add((root, cq, cb) -> cb.like(root.get("falconCode"), "%" + code + "%")));
                        Optional.ofNullable(orderCustomerName).ifPresent(name -> predicates.add((root, cq, cb) -> cb.like(root.get("customerName"), "%" + name + "%")));
                    }
                });

        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List list = billSellService.findAll(result);
            list.sort(Comparator.comparing(BillSell::getCode).reversed());
            return list;
        } else {
            List<BillSell> list = Lists.newArrayList(billSellService.findAll());
            list.sort(Comparator.comparing(BillSell::getCode).reversed());
            return list;
        }

    }

    public List<BillSell> filterInside(
            final Long codeFrom,
            final Long codeTo,
            final List<PaymentMethod> paymentMethods,
            final String checkCode,
            final Long dateFrom,
            final Long dateTo,
            final Long orderCodeFrom,
            final Long orderCodeTo,
            final String orderFalconCode,
            final String orderCustomerName
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(paymentMethods).ifPresent(value -> predicates.add((root, cq, cb) -> root.get("paymentMethod").in(value)));
        Optional.ofNullable(checkCode).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("checkCode"), "%" + value + "%")));
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(orderCodeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("order").get("code"), value)));
        Optional.ofNullable(orderCodeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("order").get("code"), value)));
        Optional.ofNullable(orderFalconCode).ifPresent(code -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("code"), "%" + code + "%")));
        Optional.ofNullable(orderCustomerName).ifPresent(name -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("customer").get("name"), "%" + name + "%")));

        if (!predicates.isEmpty()) {
            predicates.add((root, cq, cb) -> cb.isNotNull(root.get("order")));
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List list = billSellService.findAll(result);
            list.sort(Comparator.comparing(BillSell::getCode).reversed());
            return list;
        } else {
            return new ArrayList<>();
        }

    }

    public List<BillSell> filterOutside(
            final Long codeFrom,
            final Long codeTo,
            final List<PaymentMethod> paymentMethods,
            final String checkCode,
            final Long dateFrom,
            final Long dateTo,
            final String orderFalconCode,
            final String orderCustomerName
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(paymentMethods).ifPresent(value -> predicates.add((root, cq, cb) -> root.get("paymentMethod").in(value)));
        Optional.ofNullable(checkCode).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("checkCode"), "%" + value + "%")));
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(orderFalconCode).ifPresent(code -> predicates.add((root, cq, cb) -> cb.like(root.get("falconCode"), "%" + code + "%")));
        Optional.ofNullable(orderCustomerName).ifPresent(name -> predicates.add((root, cq, cb) -> cb.like(root.get("customerName"), "%" + name + "%")));

        if (!predicates.isEmpty()) {
            predicates.add((root, cq, cb) -> cb.isNull(root.get("order")));
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List list = billSellService.findAll(result);
            list.sort(Comparator.comparing(BillSell::getCode).reversed());
            return list;
        } else {
            return new ArrayList<>();
        }

    }

    public List<BillSell> findInsideSalesByToday() {
        List<Specification> predicates = new ArrayList<>();
        DateTime today = new DateTime().withTimeAtStartOfDay();
        DateTime tomorrow = new DateTime().plusDays(1).withTimeAtStartOfDay();
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), today.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), tomorrow.toDate()));
        predicates.add((root, cq, cb) -> cb.isNotNull(root.get("order")));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = billSellService.findAll(result);
        list.sort(Comparator.comparing(BillSell::getCode).reversed());
        return list;
    }

    public List<BillSell> findInsideSalesByWeek() {
        List<Specification> predicates = new ArrayList<>();
        Date weekStart = DateConverter.getCurrentWeekStart();
        Date weekEnd = DateConverter.getCurrentWeekEnd();
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), weekStart));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), weekEnd));
        predicates.add((root, cq, cb) -> cb.isNotNull(root.get("order")));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = billSellService.findAll(result);
        list.sort(Comparator.comparing(BillSell::getCode).reversed());
        return list;
    }

    public List<BillSell> findInsideSalesByMonth() {
        List<Specification> predicates = new ArrayList<>();
        DateTime monthStart = new DateTime().withTimeAtStartOfDay().withDayOfMonth(1);
        DateTime monthEnd = monthStart.plusMonths(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), monthStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), monthEnd.toDate()));
        predicates.add((root, cq, cb) -> cb.isNotNull(root.get("order")));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = billSellService.findAll(result);
        list.sort(Comparator.comparing(BillSell::getCode).reversed());
        return list;
    }

    public List<BillSell> findInsideSalesByYear() {
        List<Specification> predicates = new ArrayList<>();
        DateTime yearStart = new DateTime().withTimeAtStartOfDay().withDayOfYear(1);
        DateTime yearEnd = yearStart.plusYears(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), yearStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), yearEnd.toDate()));
        predicates.add((root, cq, cb) -> cb.isNotNull(root.get("order")));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = billSellService.findAll(result);
        list.sort(Comparator.comparing(BillSell::getCode).reversed());
        return list;
    }

    public List<BillSell> findOutsideSalesByToday() {
        List<Specification> predicates = new ArrayList<>();
        DateTime today = new DateTime().withTimeAtStartOfDay();
        DateTime tomorrow = new DateTime().plusDays(1).withTimeAtStartOfDay();
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), today.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), tomorrow.toDate()));
        predicates.add((root, cq, cb) -> cb.isNull(root.get("order")));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = billSellService.findAll(result);
        list.sort(Comparator.comparing(BillSell::getCode).reversed());
        return list;
    }

    public List<BillSell> findOutsideSalesByWeek() {
        List<Specification> predicates = new ArrayList<>();
        Date weekStart = DateConverter.getCurrentWeekStart();
        Date weekEnd = DateConverter.getCurrentWeekEnd();
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), weekStart));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), weekEnd));
        predicates.add((root, cq, cb) -> cb.isNull(root.get("order")));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = billSellService.findAll(result);
        list.sort(Comparator.comparing(BillSell::getCode).reversed());
        return list;
    }

    public List<BillSell> findOutsideSalesByMonth() {
        List<Specification> predicates = new ArrayList<>();
        DateTime monthStart = new DateTime().withTimeAtStartOfDay().withDayOfMonth(1);
        DateTime monthEnd = monthStart.plusMonths(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), monthStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), monthEnd.toDate()));
        predicates.add((root, cq, cb) -> cb.isNull(root.get("order")));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = billSellService.findAll(result);
        list.sort(Comparator.comparing(BillSell::getCode).reversed());
        return list;
    }

    public List<BillSell> findOutsideSalesByYear() {
        List<Specification> predicates = new ArrayList<>();
        DateTime yearStart = new DateTime().withTimeAtStartOfDay().withDayOfYear(1);
        DateTime yearEnd = yearStart.plusYears(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), yearStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), yearEnd.toDate()));
        predicates.add((root, cq, cb) -> cb.isNull(root.get("order")));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List list = billSellService.findAll(result);
        list.sort(Comparator.comparing(BillSell::getCode).reversed());
        return list;
    }
}
