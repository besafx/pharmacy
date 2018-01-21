package com.besafx.app.search;

import com.besafx.app.entity.BillSellReceipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.service.BillSellReceiptService;
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
public class BillSellReceiptSearch {

    private final Logger log = LoggerFactory.getLogger(BillSellReceiptSearch.class);

    @Autowired
    private BillSellReceiptService billSellReceiptService;

    public List<BillSellReceipt> filterInside(
            final Long codeFrom,
            final Long codeTo,
            final Long dateFrom,
            final Long dateTo,
            final Long orderCodeFrom,
            final Long orderCodeTo,
            final String orderFalconCode,
            final String orderCustomerName
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("billSell").get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("billSell").get("code"), value)));
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("billSell").get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("billSell").get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(orderCodeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("billSell").get("order").get("code"), value)));
        Optional.ofNullable(orderCodeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("billSell").get("order").get("code"), value)));
        Optional.ofNullable(orderFalconCode).ifPresent(code -> predicates.add((root, cq, cb) -> cb.like(root.get("billSell").get("order").get("falcon").get("code"), "%" + code + "%")));
        Optional.ofNullable(orderCustomerName).ifPresent(name -> predicates.add((root, cq, cb) -> cb.like(root.get("billSell").get("order").get("falcon").get("customer").get("name"), "%" + name + "%")));

        if (!predicates.isEmpty()) {
            predicates.add((root, cq, cb) -> cb.isNotNull(root.get("billSell").get("order")));
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List<BillSellReceipt> list = billSellReceiptService.findAll(result);
            list.sort(Comparator.comparing(billSellReceipt -> billSellReceipt.getBillSell().getCode()));
            return list;
        } else {
            return new ArrayList<>();
        }

    }

    public List<BillSellReceipt> filterOutside(
            final Long codeFrom,
            final Long codeTo,
            final Long dateFrom,
            final Long dateTo,
            final String orderFalconCode,
            final String orderCustomerName
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("billSell").get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("billSell").get("code"), value)));
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("billSell").get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("billSell").get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(orderFalconCode).ifPresent(code -> predicates.add((root, cq, cb) -> cb.like(root.get("billSell").get("falconCode"), "%" + code + "%")));
        Optional.ofNullable(orderCustomerName).ifPresent(name -> predicates.add((root, cq, cb) -> cb.like(root.get("billSell").get("customerName"), "%" + name + "%")));

        if (!predicates.isEmpty()) {
            predicates.add((root, cq, cb) -> cb.isNull(root.get("billSell").get("order")));
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List<BillSellReceipt> list = billSellReceiptService.findAll(result);
            list.sort(Comparator.comparing(billSellReceipt -> billSellReceipt.getBillSell().getCode()));
            return list;
        } else {
            return new ArrayList<>();
        }

    }
}
