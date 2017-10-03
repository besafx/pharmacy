package com.besafx.app.report;

import com.besafx.app.service.BillSellService;
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

@RestController
public class ReportBillSellController {

    private final static Logger log = LoggerFactory.getLogger(ReportBillSellController.class);

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/billSell/{id}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportBillSell(
            @PathVariable("id") Long id,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("billSell", billSellService.findOne(id));

        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/Report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export(exportType, response, jasperPrint);
    }


    @RequestMapping(value = "/report/billSells", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportBillSells(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("billSells", billSellService.findByIdIn(ids));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر للمبيعات حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/ReportBillSells.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export(exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/billSellsDetails", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportBillSellsDetails(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل للمبيعات حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/ReportBillSellsDetails.jrxml");
        ClassPathResource jrxmlFileSub = new ClassPathResource("/report/billSell/ReportBillSellsDetailsSub.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperReport jasperReportSub = JasperCompileManager.compileReport(jrxmlFileSub.getInputStream());
        map.put("subReport", jasperReportSub);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(billSellService.findByIdIn(ids)));
        reportExporter.export(exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/billSellsByDate", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportBillSellsByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("billSells", billSellService.findByDateBetween(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(), new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate()));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر للمبيعات حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/ReportBillSells.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export(exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/billSellsDetailsByDate", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportBillSellsDetailsByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل للمبيعات حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/ReportBillSellsDetails.jrxml");
        ClassPathResource jrxmlFileSub = new ClassPathResource("/report/billSell/ReportBillSellsDetailsSub.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperReport jasperReportSub = JasperCompileManager.compileReport(jrxmlFileSub.getInputStream());
        map.put("subReport", jasperReportSub);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map,
                new JRBeanCollectionDataSource(billSellService.findByDateBetween(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(),
                        new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate())));
        reportExporter.export(exportType, response, jasperPrint);
    }

}
