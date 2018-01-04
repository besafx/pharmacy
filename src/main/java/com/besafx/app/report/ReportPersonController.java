package com.besafx.app.report;

import com.besafx.app.config.CustomException;
import com.besafx.app.service.CompanyService;
import com.besafx.app.service.PersonService;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ReportPersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/report/Persons", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    public void ReportPersons(@RequestParam("personsList") List<Long> personsList, HttpServletResponse response) throws JRException, IOException {
        if (personsList.isEmpty()) {
            throw new CustomException("عفواً، فضلاً اختر على الأقل مستخدم واحد للطباعة");
        }
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=Persons.pdf");
        final OutputStream outStream = response.getOutputStream();
        /**
         * Insert Parameters
         */
        Map<String, Object> map = new HashMap<>();
        StringBuilder param1 = new StringBuilder();
        param1.append("طيف العربية");
        param1.append("\n");
        param1.append("للتعليم والتدريب التقني");
        param1.append("\n");
        param1.append("تقرير مختصر عن المستخدمين");
        map.put("TITLE", param1.toString());
        map.put("PERSONS", personsList.stream().map(value -> personService.findOne(value)).collect(Collectors.toList()));
        Lists.newArrayList(companyService.findAll()).stream().findAny().ifPresent(company -> {
            map.put("COMPANY_NAME", company.getName());
            map.put("COMPANY_PHONE", "الهاتف: " + company.getPhone());
            map.put("COMPANY_MOBILE", "الجوال: " + company.getMobile());
            map.put("COMPANY_FAX", "الفاكس: " + company.getFax());
            map.put("COMPANY_COMMERCIAL_REGISTER", "السجل التجاري: " + company.getCommericalRegisteration());
        });
        ClassPathResource jrxmlFile = new ClassPathResource("/report/person/Persons.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
    }

}
