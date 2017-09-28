package com.besafx.app.search;

import com.besafx.app.entity.BillSell;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.service.BillSellService;
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
public class BillSellSearch {

    private final Logger log = LoggerFactory.getLogger(BillSellSearch.class);

    @Autowired
    private BillSellService billSellService;

    public List<BillSell> filter(
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
        Optional.ofNullable(orderFalconCode).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("code"), "%" + value + "%")));
        Optional.ofNullable(orderCustomerName).ifPresent(value -> predicates.add((root, cq, cb) -> cb.like(root.get("order").get("falcon").get("customer").get("name"), "%" + value + "%")));
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
}
