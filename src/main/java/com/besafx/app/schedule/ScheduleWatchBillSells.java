package com.besafx.app.schedule;

import com.besafx.app.Async.AsyncScheduleDailyInsideSales;
import com.besafx.app.Async.AsyncScheduleDailyStocks;
import com.besafx.app.component.QuickEmail;
import com.besafx.app.config.DropboxManager;
import com.besafx.app.service.CompanyService;
import com.besafx.app.util.DateConverter;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@Service
public class ScheduleWatchBillSells {

    private final Logger log = LoggerFactory.getLogger(ScheduleWatchBillSells.class);

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AsyncScheduleDailyInsideSales asyncScheduleDailyInsideSales;

    @Autowired
    private AsyncScheduleDailyInsideSales asyncScheduleDailyOutsideSales;

    @Autowired
    private AsyncScheduleDailyStocks asyncScheduleDailyStocks;

    @Autowired
    private QuickEmail quickEmail;

    @Autowired
    private DropboxManager dropboxManager;

    private void run(String timeType) throws Exception {

        Future<byte[]> task = getZipFile(timeType);
        byte[] bytes = task.get();

        if(task.isDone()){

            log.info("Starting uploading zip file");
            StringBuffer fileName = new StringBuffer();
            fileName.append(DateConverter.getNowFileName());
            fileName.append(".zip");

            Future<Boolean> uploadTask = dropboxManager.uploadFile(new ByteArrayInputStream(bytes), fileName.toString(), "/Pharmacy4Falcon/WatchSales/" + fileName.toString());

            if (uploadTask.get()) {
                log.info("Ending uploading file");
                log.info("Starting sending message");
                Future<String> uploadFileLinkTask = dropboxManager.shareFile("/Pharmacy4Falcon/WatchSales/" + fileName.toString());
                uploadFileLinkTask.get();

                String subject = "مبيعات يوم ".concat(DateConverter.getHijriTodayDateString());
                List<String> emails = Lists.newArrayList(companyService.findFirstBy().getEmail(), "islamhaker@gmail.com");
                String title = "مبيعات يوم ".concat(DateConverter.getHijriTodayString());
                String subTitle =  "الموافق ".concat(DateConverter.getHijriTodayDateString());
                String body = "اضغط على الزر اداناه لعرض التقرير";
                String buttonText = "تحميل التقرير";
                quickEmail.send(subject, emails, title, subTitle, body, uploadFileLinkTask.get(), buttonText);

                log.info("ENDING SENDING MESSAGE");
            }

        }
    }

    @Async("threadMultiplePool")
    private Future<byte[]> getZipFile(String timeType) throws Exception{
        log.info("Generate inside sales report");
        Future<byte[]> work1 = asyncScheduleDailyInsideSales.getFile(timeType);
        byte[] fileBytes1 = work1.get();

        log.info("Generate outside sales report");
        Future<byte[]> work2 = asyncScheduleDailyOutsideSales.getFile(timeType);
        byte[] fileBytes2 = work2.get();

        log.info("Generate stocks report");
        Future<byte[]> work3 = asyncScheduleDailyStocks.getFile();
        byte[] fileBytes3 = work3.get();

        log.info("Generate zip file");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        ZipEntry entry1 = new ZipEntry("مبيعات داخلية.pdf");
        zipOutputStream.putNextEntry(entry1);
        zipOutputStream.write(fileBytes1);
        zipOutputStream.closeEntry();

        ZipEntry entry2 = new ZipEntry("مبيعات خارجية.pdf");
        zipOutputStream.putNextEntry(entry2);
        zipOutputStream.write(fileBytes2);
        zipOutputStream.closeEntry();

        ZipEntry entry3 = new ZipEntry("مراقبة المخزون.pdf");
        zipOutputStream.putNextEntry(entry3);
        zipOutputStream.write(fileBytes3);
        zipOutputStream.closeEntry();

        zipOutputStream.finish();
        zipOutputStream.flush();
        IOUtils.closeQuietly(zipOutputStream);
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);

        return new AsyncResult<>(byteArrayOutputStream.toByteArray());
    }

    @Scheduled(cron = "0 0 23 * * *")
    public void runDaily() throws Exception {
        run("Day");
    }

    @Scheduled(cron = "0 0/30 23 * * FRI")
    public void runWeekly() throws Exception {
        run("Week");
    }

    @Scheduled(cron = "0 0/45 23 25 * *")
    public void runMonthly() throws Exception {
        run("Month");
    }


}
