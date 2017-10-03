package com.besafx.app.report;

import com.besafx.app.entity.Order;
import com.besafx.app.service.BillSellService;
import com.besafx.app.service.OrderService;
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
public class ReportOrderController {

    private final static Logger log = LoggerFactory.getLogger(ReportOrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private BillSellService billSellService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/order/pending/{id}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportOrderPending(
            @PathVariable("id") Long id,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("order", orderService.findOne(id));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/order/ReportOrderPending.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export(exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/order/diagnosed/{id}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportOrderDiagnosed(
            @PathVariable("id") Long id,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Order order = orderService.findOne(id);
        Map<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/order/ReportOrderDiagnosed.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export(exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/order/done/{orderId}/{billSellId}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportOrderDone(
            @PathVariable("orderId") Long orderId,
            @PathVariable("billSellId") Long billSellId,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("order", orderService.findOne(orderId));
        map.put("billSell", billSellService.findOne(billSellId));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/order/ReportOrderDone.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export(exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/orders", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportOrders(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orders", orderService.findByIdIn(ids));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر لطلبات الفحص حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/order/ReportOrders.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export(exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/ordersDetails", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportOrdersDetails(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل لطلبات الفحص حسب القائمة");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/order/ReportOrdersDetails.jrxml");
        ClassPathResource jrxmlFileSub = new ClassPathResource("/report/order/ReportOrdersDetailsSub.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperReport jasperReportSub = JasperCompileManager.compileReport(jrxmlFileSub.getInputStream());
        map.put("subReport", jasperReportSub);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(orderService.findByIdIn(ids)));
        reportExporter.export(exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/ordersByDate", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportOrdersByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orders", orderService.findByDateBetween(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(), new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate()));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر لطلبات الفحص حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/order/ReportOrders.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export(exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/ordersDetailsByDate", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportOrdersDetailsByDate(
            @RequestParam(value = "dateFrom") Long dateFrom,
            @RequestParam(value = "dateTo") Long dateTo,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل لطلبات الفحص حسب الفترة من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateFrom));
        title.append(" ");
        title.append("إلى الفترة");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(dateTo));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/order/ReportOrdersDetails.jrxml");
        ClassPathResource jrxmlFileSub = new ClassPathResource("/report/order/ReportOrdersDetailsSub.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperReport jasperReportSub = JasperCompileManager.compileReport(jrxmlFileSub.getInputStream());
        map.put("subReport", jasperReportSub);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map,
                new JRBeanCollectionDataSource(orderService.findByDateBetween(new DateTime(dateFrom).withTimeAtStartOfDay().toDate(),
                        new DateTime(dateTo).plusDays(1).withTimeAtStartOfDay().toDate())));
        reportExporter.export(exportType, response, jasperPrint);
    }

}
