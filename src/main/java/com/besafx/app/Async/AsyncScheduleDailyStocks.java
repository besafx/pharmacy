package com.besafx.app.Async;

import net.sf.jasperreports.engine.*;
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
public class AsyncScheduleDailyStocks {

    private final Logger log = LoggerFactory.getLogger(AsyncScheduleDailyStocks.class);

    @Autowired
    private TransactionalService transactionalService;

    @Async("threadMultiplePool")
    public Future<byte[]> getFile() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("drugs", transactionalService.getAllDrugs());
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/drug/DrugsSummary.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        return new AsyncResult<>(JasperExportManager.exportReportToPdf(jasperPrint));
    }
}
