package com.besafx.app.search;

import com.besafx.app.entity.FundReceipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.service.FundReceiptService;
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
public class FundReceiptSearch {

    private final Logger log = LoggerFactory.getLogger(FundReceiptSearch.class);

    @Autowired
    private FundReceiptService fundReceiptService;

    public List<FundReceipt> filter(
            /**Fund Filters*/
            final Long fundCodeFrom,
            final Long fundCodeTo,

            /**Receipt Filters*/
            final ReceiptType receiptType,
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

        /**Fund Filters*/
        Optional.ofNullable(fundCodeFrom)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.greaterThanOrEqualTo(root.get("fund").get("code"), value)));

        Optional.ofNullable(fundCodeTo)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.lessThanOrEqualTo(root.get("fund").get("code"), value)));

        /**Receipt Filters*/
        Optional.ofNullable(receiptType)
                .ifPresent(value -> predicates.add((root, cq, cb) ->
                        cb.equal(root.get("receipt").get("receiptType"), value)));

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
            List<FundReceipt> list = fundReceiptService.findAll(result);
            list.sort(Comparator.comparing(fundReceipt -> fundReceipt.getReceipt().getCode()));
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public List<FundReceipt> findByToday(final ReceiptType receiptType) {
        List<Specification> predicates = new ArrayList<>();
        DateTime today = new DateTime().withTimeAtStartOfDay();
        DateTime tomorrow = new DateTime().plusDays(1).withTimeAtStartOfDay();
        predicates.add((root, cq, cb) -> cb.equal(root.get("receipt").get("receiptType"), receiptType));
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("receipt").get("date"), today.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("receipt").get("date"), tomorrow.toDate()));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List<FundReceipt> list = fundReceiptService.findAll(result);
        list.sort(Comparator.comparing(fundReceipt -> fundReceipt.getReceipt().getCode()));
        return list;
    }

    public List<FundReceipt> findByWeek(final ReceiptType receiptType) {
        List<Specification> predicates = new ArrayList<>();
        Date weekStart = DateConverter.getCurrentWeekStart();
        Date weekEnd = DateConverter.getCurrentWeekEnd();
        predicates.add((root, cq, cb) -> cb.equal(root.get("receipt").get("receiptType"), receiptType));
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("receipt").get("date"), weekStart));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("receipt").get("date"), weekEnd));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List<FundReceipt> list = fundReceiptService.findAll(result);
        list.sort(Comparator.comparing(fundReceipt -> fundReceipt.getReceipt().getCode()));
        return list;
    }

    public List<FundReceipt> findByMonth(final ReceiptType receiptType) {
        List<Specification> predicates = new ArrayList<>();
        DateTime monthStart = new DateTime().withTimeAtStartOfDay().withDayOfMonth(1);
        DateTime monthEnd = monthStart.plusMonths(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.equal(root.get("receipt").get("receiptType"), receiptType));
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("receipt").get("date"), monthStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("receipt").get("date"), monthEnd.toDate()));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List<FundReceipt> list = fundReceiptService.findAll(result);
        list.sort(Comparator.comparing(fundReceipt -> fundReceipt.getReceipt().getCode()));
        return list;
    }

    public List<FundReceipt> findByYear(final ReceiptType receiptType) {
        List<Specification> predicates = new ArrayList<>();
        DateTime yearStart = new DateTime().withTimeAtStartOfDay().withDayOfYear(1);
        DateTime yearEnd = yearStart.plusYears(1).minusDays(1);
        predicates.add((root, cq, cb) -> cb.equal(root.get("receipt").get("receiptType"), receiptType));
        predicates.add((root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("receipt").get("date"), yearStart.toDate()));
        predicates.add((root, cq, cb) -> cb.lessThanOrEqualTo(root.get("receipt").get("date"), yearEnd.toDate()));
        Specification result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = Specifications.where(result).and(predicates.get(i));
        }
        List<FundReceipt> list = fundReceiptService.findAll(result);
        list.sort(Comparator.comparing(fundReceipt -> fundReceipt.getReceipt().getCode()));
        return list;
    }
}
