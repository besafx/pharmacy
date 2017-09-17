package com.besafx.app.report;

import com.besafx.app.service.OrderService;
import com.besafx.app.util.WrapperUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ReportOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/order/{id}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportOrder(
            @PathVariable("id") Long id,
            @PathVariable(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        /**
         * Insert Parameters
         */
        Map<String, Object> map = new HashMap<>();
        /**
         * Insert Data
         */
        List<WrapperUtil> list = new ArrayList<>();
        map.put("itemDataSource", new JRBeanCollectionDataSource(list));
        map.put("order", orderService.findOne(id));
        ClassPathResource jrxmlFile = new ClassPathResource("/report/order/Report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export(exportType, response, jasperPrint);
    }

}
