package com.besafx.app;

import com.besafx.app.schedule.ScheduleWatchBillSells;
import com.besafx.app.schedule.ScheduleWatchOrders;
import com.besafx.app.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTests {

    private final Logger log = LoggerFactory.getLogger(MainTests.class);

    @Autowired
    private ScheduleWatchOrders scheduleWatchOrders;

    @Autowired
    private ScheduleWatchBillSells scheduleWatchBillSells;

    @Autowired
    private OrderReceiptService orderReceiptService;

    @Autowired
    private BillSellReceiptService billSellReceiptService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private FundService fundService;

    @Test
    public void contextLoads() throws Exception {
        orderReceiptService.findAll().forEach(orderReceipt -> {
            orderReceipt.setFund(fundService.findFirstBy());
            orderReceiptService.save(orderReceipt);
        });

        billSellReceiptService.findAll().forEach(billSellReceipt -> {
            billSellReceipt.setFund(fundService.findFirstBy());
            billSellReceiptService.save(billSellReceipt);
        });
    }
}
