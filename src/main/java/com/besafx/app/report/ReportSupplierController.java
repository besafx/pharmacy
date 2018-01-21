package com.besafx.app.report;

import com.besafx.app.entity.Supplier;
import com.besafx.app.service.SupplierService;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReportSupplierController {

    private final static Logger log = LoggerFactory.getLogger(ReportSupplierController.class);

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/supplier/details/{id}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printSupplierDetails(
            @PathVariable("id") Long id,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Supplier supplier = supplierService.findOne(id);
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("كشف حساب مورد");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/supplier/SupplierDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(Lists.newArrayList(supplier)));
        reportExporter.export("SUPPLIER_DETAILS_" + supplier.getCode(), exportType, response, jasperPrint);
    }

}
