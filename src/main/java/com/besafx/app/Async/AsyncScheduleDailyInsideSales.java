package com.besafx.app.Async;

import com.besafx.app.service.BillSellService;
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
public class AsyncScheduleDailyInsideSales {

    private final Logger log = LoggerFactory.getLogger(AsyncScheduleDailyInsideSales.class);

    private DateTime startDate, endDate;

    @Autowired
    private BillSellService billSellService;

    @Async("threadMultiplePool")
    public Future<byte[]> getFile(String timeType) throws Exception {
        StringBuilder title = new StringBuilder();
        switch (timeType) {
            case "Day":
                startDate = new DateTime().withTimeAtStartOfDay();
                endDate = new DateTime().plusDays(1).withTimeAtStartOfDay();
                title.append("تقرير يومي للمبيعات الداخلية حسب الفترة من");
                title.append(" ");
                break;
            case "Week":
                startDate = new DateTime(DateConverter.getCurrentWeekStart()).withTimeAtStartOfDay();
                endDate = new DateTime(DateConverter.getCurrentWeekEnd()).plusDays(1).withTimeAtStartOfDay();
                title.append("تقرير اسبوعي للمبيعات الداخلية حسب الفترة من");
                title.append(" ");
                break;
            case "Month":
                startDate = new DateTime().withDayOfMonth(1).withTimeAtStartOfDay();
                endDate = startDate.plusMonths(1).minusDays(1);
                title.append("تقرير شهري للمبيعات الداخلية حسب الفترة من");
                title.append(" ");
                break;
            case "Year":
                startDate = new DateTime().withDayOfYear(1).withTimeAtStartOfDay();
                endDate = startDate.plusYears(1).minusDays(1);
                title.append("تقرير سنوي للمبيعات الداخلية حسب الفترة من");
                title.append(" ");
                break;
        }
        DateTime startDate = new DateTime().withTimeAtStartOfDay();
        DateTime endDate = new DateTime().plusDays(1).withTimeAtStartOfDay();
        Map<String, Object> map = new HashMap<>();
        map.put("billSells", billSellService.findByDateBetween(startDate.toDate(), endDate.toDate()));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());

        title.append(DateConverter.getHijriStringFromDateLTR(startDate.toDate()));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(endDate.toDate()));

        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/InsideSalesSummary.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        return new AsyncResult<>(JasperExportManager.exportReportToPdf(jasperPrint));
    }
}
