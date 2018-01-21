package com.besafx.app.report;

import com.besafx.app.entity.BillBuy;
import com.besafx.app.service.BillBuyService;
import com.besafx.app.util.DateConverter;
import com.google.common.collect.Lists;
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
public class ReportBillBuyController {

    private final static Logger log = LoggerFactory.getLogger(ReportBillBuyController.class);

    @Autowired
    private BillBuyService billBuyService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/billBuy/{id}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printBillBuy(
            @PathVariable("id") Long id,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        BillBuy billBuy = billBuyService.findOne(id);
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billBuy/BillBuy.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(Lists.newArrayList(billBuy)));
        reportExporter.export("BILL_BUY_" + billBuy.getCode(), exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/billBuy/list", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printBillBuysSummaryByList(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("billBuys", billBuyService.findByIdIn(ids));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر للمشتريات حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billBuy/BillBuysSummary.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("BILL_BUYS_SUMMARY_LIST" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/billBuy/date", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printBillBuysSummaryByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("billBuys", billBuyService.findByDateBetween(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(), new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate()));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر للمشتريات حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billBuy/BillBuysSummary.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("BILL_BUYS_SUMMARY_DATE" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/billBuy/details/list", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printBillBuysDetailsByList(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل للمشتريات حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billBuy/BillBuysDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(billBuyService.findByIdIn(ids)));
        reportExporter.export("BILL_BUYS_DETAILS_LIST" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/billBuy/details/date", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printBillBuysDetailsByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل للمشتريات حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billBuy/BillBuysDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(billBuyService.findByDateBetween(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(), new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate())));
        reportExporter.export("BILL_BUYS_DETAILS_DATE" ,exportType, response, jasperPrint);
    }
}
