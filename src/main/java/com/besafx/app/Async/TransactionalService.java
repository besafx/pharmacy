package com.besafx.app.Async;

import com.besafx.app.entity.BillSell;
import com.besafx.app.entity.BillSellReceipt;
import com.besafx.app.entity.Drug;
import com.besafx.app.entity.Order;
import com.besafx.app.service.BillSellService;
import com.besafx.app.service.DrugService;
import com.besafx.app.service.OrderService;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class TransactionalService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DrugService drugService;

    @Autowired
    private BillSellService billSellService;

    @Transactional
    public List<Order> getOrders(Date dateFrom, Date dateTo) {
        List<Order> list = new ArrayList<>();
        orderService.findByDateBetween(dateFrom, dateTo).forEach(order -> {
            order.getNetCost();
            order.getPaid();
            order.getRemain();
            list.add(order);
        });
        return list;
    }

    @Transactional
    public List<Order> getOrdersDebt(Date dateFrom, Date dateTo) {
        List<Order> list = new ArrayList<>();
        orderService.findByDateBetween(dateFrom, dateTo)
                .stream()
                .filter(order -> order.getRemain() > 0).forEach(order -> {
            order.getNetCost();
            order.getPaid();
            order.getRemain();
            list.add(order);
        });
        return list;
    }

    @Transactional
    public List<Drug> getAllDrugs() {
        List<Drug> list = new ArrayList<>();
        Lists.newArrayList(drugService.findAll()).
                stream()
                .sorted(Comparator.comparing(Drug::getRealQuantitySum))
                .forEach(drug -> {
            drug.getTransactionBuysSum();
            drug.getTransactionSellsSum();
            drug.getRealQuantitySum();
            list.add(drug);
        });
        return list;
    }

    @Transactional
    public List<BillSell> getInsideSales(Date dateFrom, Date dateTo) {
        List<BillSell> list = new ArrayList<>();
        billSellService.findByDateBetweenAndOrderIsNotNull(dateFrom, dateTo)
                .stream()
                .forEach(billSell -> {
                    billSell.getNet();
                    billSell.getPaid();
                    billSell.getRemain();
                    list.add(billSell);
                });
        return list;
    }

    @Transactional
    public List<BillSell> getInsideSalesDebt(Date dateFrom, Date dateTo) {
        List<BillSell> list = new ArrayList<>();
        billSellService.findByDateBetweenAndOrderIsNotNull(dateFrom, dateTo)
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
    public List<BillSell> getOutsideSales(Date dateFrom, Date dateTo) {
        List<BillSell> list = new ArrayList<>();
        billSellService.findByDateBetweenAndOrderIsNull(dateFrom, dateTo)
                .stream()
                .forEach(billSell -> {
                    billSell.getNet();
                    billSell.getPaid();
                    billSell.getRemain();
                    list.add(billSell);
                });
        return list;
    }

    @Transactional
    public List<BillSell> getOutsideSalesDebt(Date dateFrom, Date dateTo) {
        List<BillSell> list = new ArrayList<>();
        billSellService.findByDateBetweenAndOrderIsNull(dateFrom, dateTo)
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
    public Double getTotalOrdersDebt() {
        return Lists.newArrayList(orderService.findAll())
                .stream()
                .mapToDouble(Order::getRemain)
                .sum();
    }

    @Transactional
    public Double getTotalBillSellDebt() {
        return Lists.newArrayList(billSellService.findAll())
                .stream()
                .mapToDouble(BillSell::getRemain)
                .sum();
    }


}
