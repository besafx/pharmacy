package com.besafx.app.search;

import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.service.ReceiptService;
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
public class ReceiptSearch {

    private final Logger log = LoggerFactory.getLogger(ReceiptSearch.class);

    @Autowired
    private ReceiptService receiptService;

    public List<Receipt> filter(
            final Long code,
            final Long dateFrom,
            final Long dateTo,
            final Long lastUpdateFrom,
            final Long lastUpdateTo,
            final List<PaymentMethod> paymentMethods,
            final Long checkCode,
            final Double amountNumberFrom,
            final Double amountNumberTo,
            final List<ReceiptType> receiptTypes,
            final List<Long> personIds
    ) {
        List<Specification> predicates = new ArrayList<>();

        Optional.ofNullable(code)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.like(root.get("code").as(String.class), "%" + value + "%")));

        Optional.ofNullable(dateFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.greaterThanOrEqualTo(root.get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(dateTo)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.lessThanOrEqualTo(root.get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(lastUpdateFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.greaterThanOrEqualTo(root.get("lastUpdate"), new DateTime(value).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(lastUpdateTo)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.lessThanOrEqualTo(root.get("lastUpdate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(paymentMethods)
                .ifPresent(value -> predicates.add((root, cq, cb) -> root.get("paymentMethod").in(value)));

        Optional.ofNullable(checkCode)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.like(root.get("checkCode").as(String.class), "%" + value + "%")));

        Optional.ofNullable(amountNumberFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.greaterThanOrEqualTo(root.get("amountNumber"), value)));

        Optional.ofNullable(amountNumberTo)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.lessThanOrEqualTo(root.get("amountNumber"), value)));

        Optional.ofNullable(receiptTypes)
                .ifPresent(value -> predicates.add((root, cq, cb) -> root.get("receiptType").in(value)));

        Optional.ofNullable(personIds)
                .ifPresent(value -> predicates.add((root, cq, cb) -> root.get("lastPerson").get("id").in(value)));


        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List list = receiptService.findAll(result);
            list.sort(Comparator.comparing(Receipt::getCode));
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
