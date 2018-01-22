package com.besafx.app.report;

import com.besafx.app.entity.BillBuy;
import com.besafx.app.entity.Drug;
import com.besafx.app.service.DrugService;
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
import java.util.List;
import java.util.Map;

@RestController
public class ReportDrugController {

    private final static Logger log = LoggerFactory.getLogger(ReportDrugController.class);

    @Autowired
    private DrugService drugService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/drugs", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportDrugs(
            @RequestParam(value = "ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("drugs", drugService.findByIdIn(ids));
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مختصر بالأدوية والأصناف");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/drug/ReportDrugs.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("Report" ,exportType, response, jasperPrint);
    }

    @RequestMapping(value = "/report/drugs/details", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printDrugDetails(
            @RequestParam(value = "ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/drug/DrugDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(drugService.findByIdIn(ids)));
        reportExporter.export("DRUG_DETAILS", exportType, response, jasperPrint);
    }

}
