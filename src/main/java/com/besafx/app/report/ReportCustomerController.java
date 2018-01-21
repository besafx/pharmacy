package com.besafx.app.report;

import com.besafx.app.entity.Customer;
import com.besafx.app.service.CustomerService;
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
public class ReportCustomerController {

    private final static Logger log = LoggerFactory.getLogger(ReportCustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/customer/details/{id}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printCustomerDetails(
            @PathVariable("id") Long id,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Customer customer = customerService.findOne(id);
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("كشف حساب عميل");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/customer/CustomerDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(Lists.newArrayList(customer)));
        reportExporter.export("CUSTOMER_DETAILS_" + customer.getCode(), exportType, response, jasperPrint);
    }

}
