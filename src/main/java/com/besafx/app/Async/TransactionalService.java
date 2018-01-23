package com.besafx.app.Async;

import com.besafx.app.entity.BillSell;
import com.besafx.app.entity.Drug;
import com.besafx.app.service.BillSellService;
import com.besafx.app.service.DrugService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionalService {

    @Autowired
    private DrugService drugService;

    @Autowired
    private BillSellService billSellService;

    @Transactional
    public List<Drug> getAllDrugs() {
        List<Drug> list = new ArrayList<>();
        drugService.findAll().forEach(drug -> {
            drug.getTransactionBuysSum();
            drug.getTransactionSellsSum();
            drug.getRealQuantitySum();
            list.add(drug);
        });
        return list;
    }

    @Transactional
    public List<BillSell> getInsideSalesDebt(Long dateFrom, Long dateTo) {
        List<BillSell> list = new ArrayList<>();
        billSellService.findByDateBetweenAndOrderIsNotNull(
                new DateTime(dateFrom).withTimeAtStartOfDay().toDate(),
                new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate())
                .stream()
                .filter(billSell -> billSell.getRemain() > 0)
                .forEach(billSell -> {
            billSell.getNet();
            billSell.getPaid();
            billSell.getRemain();
            list.add(billSell);
        });
        return list;
    }

    @Transactional
    public List<BillSell> getOutsideSalesDebt(Long dateFrom, Long dateTo) {
        List<BillSell> list = new ArrayList<>();
        billSellService.findByDateBetweenAndOrderIsNull(
                new DateTime(dateFrom).withTimeAtStartOfDay().toDate(),
                new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate())
                .stream()
                .filter(billSell -> billSell.getRemain() > 0)
                .forEach(billSell -> {
                    billSell.getNet();
                    billSell.getPaid();
                    billSell.getRemain();
                    list.add(billSell);
                });
        return list;
    }
}
