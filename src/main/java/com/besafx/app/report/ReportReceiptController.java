package com.besafx.app.report;

import com.besafx.app.entity.BillSell;
import com.besafx.app.entity.Receipt;
import com.besafx.app.service.BillSellService;
import com.besafx.app.service.ReceiptService;
import com.besafx.app.util.DateConverter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ReportReceiptController {

    private final static Logger log = LoggerFactory.getLogger(ReportReceiptController.class);

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/receipt/in/{id}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printReceiptIn(
            @PathVariable("id") Long id,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Receipt receipt = receiptService.findOne(id);
        map.put("receipt", receipt);
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/receipt/ReceiptIn.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("RECEIPT_IN_" + receipt.getCode() ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/receipt/out/{id}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printReceiptOut(
            @PathVariable("id") Long id,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Receipt receipt = receiptService.findOne(id);
        map.put("receipt", receipt);
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/receipt/ReceiptOut.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("RECEIPT_OUT_" + receipt.getCode() ,exportType, response, jasperPrint);
    }

}
