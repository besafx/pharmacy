package com.besafx.app.report;

import com.besafx.app.service.FalconService;
import com.besafx.app.service.OrderService;
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
public class ReportFalconsController {

    private final static Logger log = LoggerFactory.getLogger(ReportFalconsController.class);

    @Autowired
    private FalconService falconService;

    @Autowired
    private ReportExporter reportExporter;

    @RequestMapping(value = "/report/falcons", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @ResponseBody
    public void ReportFalcons(
            @RequestParam("ids") List<Long> ids,
            @RequestParam(value = "exportType") ExportType exportType,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("logo", new ClassPathResource("/report/img/logo.png").getInputStream());
        StringBuilder title = new StringBuilder();
        title.append("تقرير مفصل للصقور");
        map.put("title", title.toString());
        ClassPathResource jrxmlFile = new ClassPathResource("/report/falcon/FalconsDetails.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getInputStream());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(falconService.findByIdIn(ids)));
        reportExporter.export("FALCONS_DETAILS" ,exportType, response, jasperPrint);
    }

}
