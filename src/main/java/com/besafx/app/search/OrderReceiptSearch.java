package com.besafx.app.search;

import com.besafx.app.entity.OrderReceipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.service.OrderReceiptService;
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
public class OrderReceiptSearch {

    private final Logger log = LoggerFactory.getLogger(OrderReceiptSearch.class);

    @Autowired
    private OrderReceiptService orderReceiptService;

    public List<OrderReceipt> filter(
            final Long orderCodeFrom,
            final Long orderCodeTo,
            final String orderCustomerName,
            final String orderCustomerMobile,
            final Long orderFalconCode,
            final String orderFalconType,
            final Long orderDateFrom,
            final Long orderDateTo,

            final Long receiptCode,
            final Long receiptDateFrom,
            final Long receiptDateTo,
            final Long receiptLastUpdateFrom,
            final Long receiptLastUpdateTo,
            final List<PaymentMethod> receiptPaymentMethods,
            final Long receiptCheckCode,
            final Double receiptAmountNumberFrom,
            final Double receiptAmountNumberTo,
            final List<ReceiptType> receiptReceiptTypes,
            final List<Long> receiptPersonIds
    ) {
        List<Specification> predicates = new ArrayList<>();

        Optional.ofNullable(orderCodeFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.greaterThanOrEqualTo(root.get("order").get("code"), value)));

        Optional.ofNullable(orderCodeTo)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.lessThanOrEqualTo(root.get("order").get("code"), value)));

        Optional.ofNullable(orderCustomerName)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.like(root.get("order").get("falcon").get("customer").get("name"), "%" + value + "%")));

        Optional.ofNullable(orderCustomerMobile)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.like(root.get("order").get("falcon").get("customer").get("mobile"), "%" + value + "%")));

        Optional.ofNullable(orderFalconCode)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.like(root.get("order").get("falcon").get("code").as(String.class), "%" + value + "%")));

        Optional.ofNullable(orderFalconType)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.like(root.get("order").get("falcon").get("type"), "%" + value + "%")));

        Optional.ofNullable(orderDateFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.greaterThanOrEqualTo(root.get("order").get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(orderDateTo)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.lessThanOrEqualTo(root.get("order").get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(receiptCode)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.like(root.get("receipt").get("code").as(String.class), "%" + value + "%")));

        Optional.ofNullable(receiptDateFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.greaterThanOrEqualTo(root.get("receipt").get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(receiptDateTo)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.lessThanOrEqualTo(root.get("receipt").get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(receiptLastUpdateFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.greaterThanOrEqualTo(root.get("receipt").get("lastUpdate"), new DateTime(value).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(receiptLastUpdateTo)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.lessThanOrEqualTo(root.get("receipt").get("lastUpdate"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));

        Optional.ofNullable(receiptPaymentMethods)
                .ifPresent(value -> predicates.add((root, cq, cb) -> root.get("receipt").get("paymentMethod").in(value)));

        Optional.ofNullable(receiptCheckCode)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.like(root.get("receipt").get("checkCode").as(String.class), "%" + value + "%")));

        Optional.ofNullable(receiptAmountNumberFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.greaterThanOrEqualTo(root.get("receipt").get("amountNumber"), value)));

        Optional.ofNullable(receiptAmountNumberTo)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.lessThanOrEqualTo(root.get("receipt").get("amountNumber"), value)));

        Optional.ofNullable(receiptReceiptTypes)
                .ifPresent(value -> predicates.add((root, cq, cb) -> root.get("receipt").get("receiptType").in(value)));

        Optional.ofNullable(receiptPersonIds)
                .ifPresent(value -> predicates.add((root, cq, cb) -> root.get("receipt").get("lastPerson").get("id").in(value)));


        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List<OrderReceipt> list = orderReceiptService.findAll(result);
            list.sort(Comparator.comparing(orderReceipt -> orderReceipt.getReceipt().getCode()));
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
