package com.besafx.app.search;

import com.besafx.app.entity.BillBuy;
import com.besafx.app.service.BillBuyService;
import com.besafx.app.util.DateConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BillBuySearch {

    private final Logger log = LoggerFactory.getLogger(BillBuySearch.class);

    @Autowired
    private BillBuyService billBuyService;

    public List<BillBuy> filter(
            final Long codeFrom,
            final Long codeTo,
            final Long dateFrom,
            final Long dateTo,
            final List<Long> suppliers
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("code"), value)));
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(suppliers).ifPresent(value -> predicates.add((root, cq, cb) -> root.get("supplier").get("id").in(value)));
        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List<BillBuy> list = billBuyService.findAll(result);
            list.sort(Comparator.comparing(BillBuy::getCode).reversed());
            return list;
        } else {
            return new ArrayList<>();
        }

    }

    public List<BillBuy> findByToday() {
        List<Specification> predicates = new ArrayList<>();
        DateTime today = new DateTime().withTimeAtStartOfDay();
        DateTime tomorrow = new DateTime().plusDays(1).withTimeAtStartOfDay();
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), today.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), tomorrow.toDate()));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List<BillBuy> list = billBuyService.findAll(result);
        list.sort(Comparator.comparing(BillBuy::getCode).reversed());
        return list;
    }

    public List<BillBuy> findByWeek() {
        List<Specification> predicates = new ArrayList<>();
        Date weekStart = DateConverter.getCurrentWeekStart();
        Date weekEnd = DateConverter.getCurrentWeekEnd();
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), weekStart));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), weekEnd));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List<BillBuy> list = billBuyService.findAll(result);
        list.sort(Comparator.comparing(BillBuy::getCode).reversed());
        return list;
    }

    public List<BillBuy> findByMonth() {
        List<Specification> predicates = new ArrayList<>();
        DateTime monthStart = new DateTime().withTimeAtStartOfDay().withDayOfMonth(1);
        DateTime monthEnd = monthStart.plusMonths(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), monthStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), monthEnd.toDate()));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List<BillBuy> list = billBuyService.findAll(result);
        list.sort(Comparator.comparing(BillBuy::getCode).reversed());
        return list;
    }

    public List<BillBuy> findByYear() {
        List<Specification> predicates = new ArrayList<>();
        DateTime yearStart = new DateTime().withTimeAtStartOfDay().withDayOfYear(1);
        DateTime yearEnd = yearStart.plusYears(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("date"), yearStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("date"), yearEnd.toDate()));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List<BillBuy> list = billBuyService.findAll(result);
        list.sort(Comparator.comparing(BillBuy::getCode).reversed());
        return list;
    }
}
