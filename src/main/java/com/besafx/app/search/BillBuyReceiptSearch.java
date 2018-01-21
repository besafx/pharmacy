package com.besafx.app.search;

import com.besafx.app.entity.BillBuyReceipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.service.BillBuyReceiptService;
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
public class BillBuyReceiptSearch {

    private final Logger log = LoggerFactory.getLogger(BillBuyReceiptSearch.class);

    @Autowired
    private BillBuyReceiptService billBuyReceiptService;

    public List<BillBuyReceipt> filter(
            final Long codeFrom,
            final Long codeTo,
            final Long dateFrom,
            final Long dateTo
    ) {
        List<Specification> predicates = new ArrayList<>();
        Optional.ofNullable(codeFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("billBuy").get("code"), value)));
        Optional.ofNullable(codeTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("billBuy").get("code"), value)));
        Optional.ofNullable(dateFrom).ifPresent(value -> predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("billBuy").get("date"), new DateTime(value).withTimeAtStartOfDay().toDate())));
        Optional.ofNullable(dateTo).ifPresent(value -> predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("billBuy").get("date"), new DateTime(value).plusDays(1).withTimeAtStartOfDay().toDate())));

        if (!predicates.isEmpty()) {
            Specification result = predicates.get(0);
            for (int i = 1; i < predicates.size(); i++) {
                result = Specifications.where(result).and(predicates.get(i));
            }
            List<BillBuyReceipt> list = billBuyReceiptService.findAll(result);
            list.sort(Comparator.comparing(billBuyReceipt -> billBuyReceipt.getBillBuy().getCode()));
            return list;
        } else {
            return new ArrayList<>();
        }

    }
}
