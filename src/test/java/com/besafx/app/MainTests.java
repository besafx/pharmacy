package com.besafx.app;

import com.besafx.app.entity.BillSellReceipt;
import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.schedule.ScheduleDailyOrders;
import com.besafx.app.service.*;
import com.besafx.app.util.ArabicLiteralNumberParser;
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
    private ScheduleDailyOrders scheduleDailyOrders;

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

    @Test
    public void contextLoads() throws Exception {
        billSellService.findAll().forEach(billSell -> {
            if (billSell.getPaymentMethod().equals(PaymentMethod.Cash)) {
                BillSellReceipt billSellReceipt = new BillSellReceipt();
                billSellReceipt.setBillSell(billSell);
                //
                Receipt receipt = new Receipt();
                receipt.setReceiptType(ReceiptType.In);
                receipt.setDate(billSell.getDate());
                receipt.setAmountNumber(billSell.getNet());
                receipt.setAmountString(ArabicLiteralNumberParser.literalValueOf(billSell.getNet()));
                Receipt topReceipt = receiptService.findTopByOrderByCodeDesc();
                if (topReceipt == null) {
                     receipt.setCode(new Long(1));
                } else {
                     receipt.setCode(topReceipt.getCode() + 1);
                }
                receipt.setNote("دفعة مالية للفاتورة رقم / " + billSell.getCode());
                receipt.setPaymentMethod(PaymentMethod.Cash);
                //
                billSellReceipt.setReceipt(receiptService.save(receipt));
                billSellReceiptService.save(billSellReceipt);
            }
        });
    }
}
