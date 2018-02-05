package com.besafx.app.report;

import com.besafx.app.auditing.Action;
import com.besafx.app.entity.Customer;
import com.besafx.app.entity.History;
import com.besafx.app.service.CustomerService;
import com.besafx.app.service.HistoryService;
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
public class ReportHistoryController {

    private final static Logger log = LoggerFactory.getLogger(ReportHistoryController.class);

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/history/{startDate}/{endDate}/{exportType}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void printCustomerDetails(
            @PathVariable("startDate") Long startDate,
            @PathVariable("endDate") Long endDate,
            @PathVariable("exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<History> histories = historyService.findByModifiedDateBetweenAndActionIn(
                new DateTime(startDate).withTimeAtStartOfDay().toDate(),
                new DateTime(endDate).plusDays(1).withTimeAtStartOfDay().toDate(),
                Lists.newArrayList(Action.UPDATED, Action.DELETED));
        map.put("histories", histories);
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("سجل العلميات على البرنامج من");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(startDate));
        title.append(" ");
        title.append("إلى");
        title.append(" ");
        title.append(DateConverter.getHijriStringFromDateLTR(endDate));
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/history/History.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map);
        reportExporter.export("HISTORY_" + startDate + "_" + endDate, exportType, response, jasperPrint);
    }

}
