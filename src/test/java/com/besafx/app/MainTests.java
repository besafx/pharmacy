package com.besafx.app;

import com.besafx.app.entity.OrderReceipt;
import com.besafx.app.entity.Receipt;
import com.besafx.app.entity.enums.PaymentMethod;
import com.besafx.app.entity.enums.ReceiptType;
import com.besafx.app.schedule.ScheduleDailyOrders;
import com.besafx.app.service.OrderReceiptService;
import com.besafx.app.service.OrderService;
import com.besafx.app.service.ReceiptService;
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
    private ReceiptService receiptService;

    @Autowired
    private OrderService orderService;

    @Test
    public void contextLoads() throws Exception {
        orderService.findAll().forEach(order -> {
            if(order.getPaymentMethod().equals(PaymentMethod.Cash)){
                OrderReceipt orderReceipt = new OrderReceipt();
                orderReceipt.setOrder(order);
                //
                Receipt receipt = new Receipt();
                receipt.setReceiptType(ReceiptType.In);
                receipt.setDate(order.getDate());
                receipt.setAmountNumber(order.getNetCost());
                receipt.setAmountString(ArabicLiteralNumberParser.literalValueOf(order.getNetCost()));
                receipt.setCode(order.getCode().longValue());
                receipt.setNote("ايراد الطلب رقم " + order.getCode());
                receipt.setPaymentMethod(PaymentMethod.Cash);
                //
                orderReceipt.setReceipt(receiptService.save(receipt));
                orderReceiptService.save(orderReceipt);
            }
        });
    }
}
