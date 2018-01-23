package com.besafx.app.report;

import com.besafx.app.entity.BillSell;
import com.besafx.app.service.BillSellService;
import com.besafx.app.util.DateConverter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ReportBillSellController {

    private final static Logger log = LoggerFactory.getLogger(ReportBillSellController.class);

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/insideBillPurchase/{id}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printInsideBillPurchase(
            @PathVariable("id") Long id,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        BillSell billSell = billSellService.findOne(id);
        map.put("billSell", billSell);
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/InsideBillPurchase.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("INSIDE_BILL_PURCHASE_" + billSell.getCode() ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/outsideBillPurchase/{id}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printOutsideBillPurchase(
            @PathVariable("id") Long id,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        BillSell billSell = billSellService.findOne(id);
        map.put("billSell", billSell);
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/OutsideBillPurchase.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("OUTSIDE_BILL_PURCHASE_" + billSell.getCode() ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/insideSales/list", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printInsideSalesSummaryByList(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("insideBills", billSellService.findByIdIn(ids));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر للمبيعات الداخلية حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/InsideSalesSummary.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("INSIDE_SALES_SUMMARY_LIST" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/insideSales/details/list", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printInsideSalesDetailsByList(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل للمبيعات الداخلية حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/InsideSalesDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(billSellService.findByIdIn(ids)));
        reportExporter.export("INSIDE_SALES_DETAILS_LIST" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/outsideSales/list", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printOutsideSalesSummaryByList(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("outsideBills", billSellService.findByIdIn(ids));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر للمبيعات الخارجية حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/OutsideSalesSummary.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("OUTSIDE_SALES_SUMMARY_LIST" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/outsideSales/details/list", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printOutsideSalesDetailsByList(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل للمبيعات الخارجية حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/OutsideSalesDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(billSellService.findByIdIn(ids)));
        reportExporter.export("OUTSIDE_SALES_DETAILS_LIST" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/insideSales/date", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printInsideSalesSummaryByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("insideBills", billSellService.findByDateBetweenAndOrderIsNotNull(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(), new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate()));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر للمبيعات الداخلية حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/InsideSalesSummary.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("INSIDE_SALES_SUMMARY_DATE" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/insideSales/details/date", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printInsideSalesDetailsByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل للمبيعات الداخلية حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/InsideSalesDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(billSellService.findByDateBetweenAndOrderIsNotNull(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(), new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate())));
        reportExporter.export("INSIDE_SALES_DETAILS_DATE" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/outsideSales/date", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printOutsideSalesSummaryByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("outsideBills", billSellService.findByDateBetweenAndOrderIsNull(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(), new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate()));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر للمبيعات الخارجية حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/OutsideSalesSummary.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("OUTSIDE_SALES_SUMMARY_DATE" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/outsideSales/details/date", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printOutsideSalesDetailsByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل للمبيعات الخارجية حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/OutsideSalesDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(billSellService.findByDateBetweenAndOrderIsNull(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(), new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate())));
        reportExporter.export("OUTSIDE_SALES_DETAILS_DATE" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/insideSales/debt", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printInsideSalesDebt(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("bills", billSellService
                .findByDateBetweenAndOrderIsNotNull(
                        new DateTime(dateFrom).withTimeAtStartOfDay().toDate(),
                        new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate())
                .stream()
                .filter(order -> order.getRemain() > 0)
                .collect(Collectors.toList())
        );
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("المطالبات المالية للمبيعات الداخلية حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/InsideSalesDebt.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("INSIDE_SALES_DEBT" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/outsideSales/debt", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printOutsideSalesDebt(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("bills", billSellService
                .findByDateBetweenAndOrderIsNull(
                        new DateTime(dateFrom).withTimeAtStartOfDay().toDate(),
                        new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate())
                .stream()
                .filter(order -> order.getRemain() > 0)
                .collect(Collectors.toList())
        );
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("المطالبات المالية للمبيعات الخارجية حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/billSell/OutsideSalesDebt.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("OUTSIDE_SALES_DEBT" ,exportType, response, jasperPrint);
    }

}
