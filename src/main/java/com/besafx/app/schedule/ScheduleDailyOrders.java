package com.besafx.app.schedule;

import com.besafx.app.Async.AsyncScheduleDailyOrders;
import com.besafx.app.config.EmailSender;
import com.besafx.app.service.CompanyService;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ScheduleDailyOrders {

    private final Logger log = LoggerFactory.getLogger(ScheduleDailyOrders.class);

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AsyncScheduleDailyOrders asyncScheduleDailyOrders;

    @Autowired
    private EmailSender emailSender;

    @Scheduled(cron = "0 0 22 * * *")
    public void run() throws Exception {
        Future<byte[]> work = asyncScheduleDailyOrders.getFile();
        byte[] fileBytes = work.get();
        if (fileBytes != null) {
            String randomFileName = "OrdersToday-" + ThreadLocalRandom.current().nextInt(1, 50000);
            File reportFile = File.createTempFile(randomFileName, ".pdf");
            FileUtils.writeByteArrayToFile(reportFile, fileBytes);
            log.info("جاري تحويل الملف");
            Thread.sleep(10000);
            Future<Boolean> mail = emailSender.send("تقرير بطلبات الفحص اليومية", "", Lists.newArrayList(companyService.findAll()).get(0).getEmail(), Lists.newArrayList(reportFile));
            mail.get();
            log.info("تم إرسال الملف فى البريد الإلكتروني بنجاح");
        }
    }


}
