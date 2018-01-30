package com.besafx.app.schedule;

import com.besafx.app.Async.*;
import com.besafx.app.component.QuickEmail;
import com.besafx.app.config.DropboxManager;
import com.besafx.app.service.CompanyService;
import com.besafx.app.util.DateConverter;
import com.google.common.collect.Lists;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Component
@Service
public class ScheduleSendingReports {

    private final Logger log = LoggerFactory.getLogger(ScheduleSendingReports.class);

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AsyncScheduleDailyOrders asyncScheduleDailyOrders;

    @Autowired
    private AsyncScheduleDailyOrdersDebt asyncScheduleDailyOrdersDebt;

    @Autowired
    private AsyncScheduleDailyInsideSales asyncScheduleDailyInsideSales;

    @Autowired
    private AsyncScheduleDailyInsideSales asyncScheduleDailyOutsideSales;

    @Autowired
    private AsyncScheduleDailyStocks asyncScheduleDailyStocks;

    @Autowired
    private AsyncScheduleDailyInsideSalesDebt asyncScheduleDailyInsideSalesDebt;

    @Autowired
    private AsyncScheduleDailyOutsideSalesDebt asyncScheduleDailyOutsideSalesDebt;

    @Autowired
    private AsyncScheduleDailyHistory asyncScheduleDailyHistory;

    @Autowired
    private QuickEmail quickEmail;

    @Autowired
    private DropboxManager dropboxManager;

    private void run(String timeType) throws Exception {

        Future<byte[]> task = getZip4jFile(timeType);
        byte[] bytes = task.get();

        if(task.isDone()){

            log.info("Starting uploading zip file");
            StringBuffer fileName = new StringBuffer();
            fileName.append(DateConverter.getNowFileName());
            fileName.append(".zip");

            Future<Boolean> uploadTask = dropboxManager.uploadFile(new ByteArrayInputStream(bytes), fileName.toString(), "/Pharmacy4Falcon/Reports/" + fileName.toString());

            if (uploadTask.get()) {
                log.info("Ending uploading file");
                log.info("Starting sending message");
                Future<String> uploadFileLinkTask = dropboxManager.shareFile("/Pharmacy4Falcon/Reports/" + fileName.toString());
                uploadFileLinkTask.get();

                String subject = "", title = "", subTitle = "";

                switch (timeType) {
                    case "Day":
                        subject = "التقارير اليومية - " + DateConverter.getNowFileName();
                        title = "التقارير اليومية - "+ DateConverter.getNowFileName();
                        subTitle =  "تقارير اليوم الموافق ".concat(DateConverter.getHijriTodayDateString());
                        break;
                    case "Week":
                        subject = "التقارير الاسبوعية - " + DateConverter.getNowFileName();
                        title = "التقارير الاسبوعية - "+ DateConverter.getNowFileName();
                        subTitle =  "تقارير الاسبوع";
                        break;
                    case "Month":
                        subject = "التقارير الشهرية - " + DateConverter.getNowFileName();
                        title = "التقارير الشهرية - "+ DateConverter.getNowFileName();
                        subTitle =  "تقارير الشهر";
                        break;
                    case "Year":
                        subject = "التقارير السنوية - " + DateConverter.getNowFileName();
                        title = "التقارير السنوية - "+ DateConverter.getNowFileName();
                        subTitle =  "تقارير العام";
                        break;
                }

                String body = "اضغط على الزر اداناه لتحميل التقارير";
                String buttonText = "تحميل التقارير";
                List<String> emails = Lists.newArrayList(companyService.findFirstBy().getEmail(), "islamhaker@gmail.com");
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

        log.info("Generate insideSalesDebt report");
        Future<byte[]> work4 = asyncScheduleDailyInsideSalesDebt.getFile(timeType);
        byte[] fileBytes4 = work4.get();

        log.info("Generate outsideSalesDebt report");
        Future<byte[]> work5 = asyncScheduleDailyOutsideSalesDebt.getFile(timeType);
        byte[] fileBytes5 = work5.get();

        log.info("Generate orders report");
        Future<byte[]> work6 = asyncScheduleDailyOrders.getFile(timeType);
        byte[] fileBytes6 = work6.get();

        log.info("Generate ordersDebt report");
        Future<byte[]> work7 = asyncScheduleDailyOrdersDebt.getFile(timeType);
        byte[] fileBytes7 = work7.get();

        log.info("Generate history report");
        Future<byte[]> work8 = asyncScheduleDailyHistory.getFile(timeType);
        byte[] fileBytes8 = work8.get();

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

        ZipEntry entry4 = new ZipEntry("المطالبات المالية للمبيعات الداخلية.pdf");
        zipOutputStream.putNextEntry(entry4);
        zipOutputStream.write(fileBytes4);
        zipOutputStream.closeEntry();

        ZipEntry entry5 = new ZipEntry("المطالبات المالية للمبيعات الخارجية.pdf");
        zipOutputStream.putNextEntry(entry5);
        zipOutputStream.write(fileBytes5);
        zipOutputStream.closeEntry();

        ZipEntry entry6 = new ZipEntry("طلبات الفحص.pdf");
        zipOutputStream.putNextEntry(entry6);
        zipOutputStream.write(fileBytes6);
        zipOutputStream.closeEntry();

        ZipEntry entry7 = new ZipEntry("مديونيات طلبات الفحص.pdf");
        zipOutputStream.putNextEntry(entry7);
        zipOutputStream.write(fileBytes7);
        zipOutputStream.closeEntry();

        ZipEntry entry8 = new ZipEntry("عمليات البرنامج.pdf");
        zipOutputStream.putNextEntry(entry8);
        zipOutputStream.write(fileBytes8);
        zipOutputStream.closeEntry();

        zipOutputStream.finish();
        zipOutputStream.flush();
        IOUtils.closeQuietly(zipOutputStream);
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);

        return new AsyncResult<>(byteArrayOutputStream.toByteArray());
    }

    @Async("threadMultiplePool")
    private Future<byte[]> getZip4jFile(String timeType) throws Exception{
        log.info("Generate inside sales report");
        Future<byte[]> work1 = asyncScheduleDailyInsideSales.getFile(timeType);
        byte[] fileBytes1 = work1.get();

        log.info("Generate outside sales report");
        Future<byte[]> work2 = asyncScheduleDailyOutsideSales.getFile(timeType);
        byte[] fileBytes2 = work2.get();

        log.info("Generate stocks report");
        Future<byte[]> work3 = asyncScheduleDailyStocks.getFile();
        byte[] fileBytes3 = work3.get();

        log.info("Generate insideSalesDebt report");
        Future<byte[]> work4 = asyncScheduleDailyInsideSalesDebt.getFile(timeType);
        byte[] fileBytes4 = work4.get();

        log.info("Generate outsideSalesDebt report");
        Future<byte[]> work5 = asyncScheduleDailyOutsideSalesDebt.getFile(timeType);
        byte[] fileBytes5 = work5.get();

        log.info("Generate orders report");
        Future<byte[]> work6 = asyncScheduleDailyOrders.getFile(timeType);
        byte[] fileBytes6 = work6.get();

        log.info("Generate ordersDebt report");
        Future<byte[]> work7 = asyncScheduleDailyOrdersDebt.getFile(timeType);
        byte[] fileBytes7 = work7.get();

        log.info("Generate history report");
        Future<byte[]> work8 = asyncScheduleDailyHistory.getFile(timeType);
        byte[] fileBytes8 = work8.get();

        log.info("Generate zip file");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        net.lingala.zip4j.io.ZipOutputStream zos = new net.lingala.zip4j.io.ZipOutputStream(bufferedOutputStream);

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);

        parameters.setFileNameInZip("مبيعات داخلية.pdf");
        parameters.setSourceExternalStream(true);
        zos.putNextEntry(null, parameters);
        zos.write(fileBytes1);
        zos.closeEntry();

        parameters.setFileNameInZip("مبيعات خارجية.pdf");
        parameters.setSourceExternalStream(true);
        zos.putNextEntry(null, parameters);
        zos.write(fileBytes2);
        zos.closeEntry();

        parameters.setFileNameInZip("مراقبة المخزون.pdf");
        parameters.setSourceExternalStream(true);
        zos.putNextEntry(null, parameters);
        zos.write(fileBytes3);
        zos.closeEntry();

        parameters.setFileNameInZip("المطالبات المالية للمبيعات الداخلية.pdf");
        parameters.setSourceExternalStream(true);
        zos.putNextEntry(null, parameters);
        zos.write(fileBytes4);
        zos.closeEntry();

        parameters.setFileNameInZip("المطالبات المالية للمبيعات الخارجية.pdf");
        parameters.setSourceExternalStream(true);
        zos.putNextEntry(null, parameters);
        zos.write(fileBytes5);
        zos.closeEntry();

        parameters.setFileNameInZip("طلبات الفحص.pdf");
        parameters.setSourceExternalStream(true);
        zos.putNextEntry(null, parameters);
        zos.write(fileBytes6);
        zos.closeEntry();

        parameters.setFileNameInZip("مديونيات طلبات الفحص.pdf");
        parameters.setSourceExternalStream(true);
        zos.putNextEntry(null, parameters);
        zos.write(fileBytes7);
        zos.closeEntry();

        parameters.setFileNameInZip("عمليات البرنامج.pdf");
        parameters.setSourceExternalStream(true);
        zos.putNextEntry(null, parameters);
        zos.write(fileBytes8);
        zos.closeEntry();

        zos.finish();
        zos.flush();
        IOUtils.closeQuietly(zos);
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);

        return new AsyncResult<>(byteArrayOutputStream.toByteArray());
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void runDaily() throws Exception {
        run("Day");
    }

    @Scheduled(cron = "0 0/30 22 * * FRI")
    public void runWeekly() throws Exception {
        run("Week");
    }

    @Scheduled(cron = "0 0/45 22 25 * *")
    public void runMonthly() throws Exception {
        run("Month");
    }


}
