package com.besafx.app.Async;

import com.besafx.app.service.BillSellService;
import com.besafx.app.service.OrderService;
import com.besafx.app.util.DateConverter;
import net.sf.jasperreports.engine.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class AsyncScheduleDailyBillSells {

    private final Logger log = LoggerFactory.getLogger(AsyncScheduleDailyBillSells.class);

    @Autowired
    private BillSellService billSellService;

    @Async("ByteGenerate")
    public Future<byte[]> getFile() throws Exception {
        DateTime startDate = new DateTime().withTimeAtStartOfDay();
        DateTime endDate = new DateTime();
        Map<String, Object> map = new HashMap<>();
        map.put("billSells", billSellService.findByDateBetween(new DateTime(startDate).withTimeAtStartOfDay().toDate(), new DateTime(endDate).plusDays(1).withTimeAtStartOfDay().toDate()));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر للمبيعات حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(startDate.toDate()));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(endDate.toDate()));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/ReportBillSells.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        return new AsyncResult<>(JasperExportManager.exportReportToPdf(jasperPrint));
    }
}
