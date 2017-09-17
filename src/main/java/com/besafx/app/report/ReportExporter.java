package com.besafx.app.report;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class ReportExporter {

    public void export(final ExportType exportType, final HttpServletResponse response, final JasperPrint jasperPrint) throws Exception {
        Exporter exporter = null;
        switch (exportType) {
            case PDF:
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"report.pdf\"");
                exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
                SimplePdfExporterConfiguration configurationPdf = new SimplePdfExporterConfiguration();
                configurationPdf.setCreatingBatchModeBookmarks(true);
                exporter.setConfiguration(configurationPdf);
                break;
            case RTF:
                response.setContentType("application/rtf");
                response.setHeader("Content-Disposition", "inline; filename=\"report.rtf\"");
                exporter = new JRRtfExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getOutputStream()));
                break;
            case HTML:
                response.setContentType("application/html");
                response.setHeader("Content-Disposition", "inline; filename=\"report.html\"");
                exporter = new HtmlExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getOutputStream()));
                SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
                reportExportConfiguration.setWhitePageBackground(false);
                reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
                exporter.setConfiguration(reportExportConfiguration);
                break;
            case XHTML:
                response.setContentType("application/xhtml");
                response.setHeader("Content-Disposition", "inline; filename=\"report.xhtml\"");
                exporter = new HtmlExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getOutputStream()));
                break;
            case XLSX:
                response.setContentType("application/xlsx");
                response.setHeader("Content-Disposition", "inline; filename=\"report.xlsx\"");
                exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
                SimpleXlsxReportConfiguration configurationXlsx = new SimpleXlsxReportConfiguration();
                configurationXlsx.setOnePagePerSheet(false);
                exporter.setConfiguration(configurationXlsx);
                break;
            case CSV:
                response.setContentType("application/csv");
                response.setHeader("Content-Disposition", "inline; filename=\"report.csv\"");
                exporter = new JRCsvExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getOutputStream()));
                break;
            case PPTX:
                response.setContentType("application/pptx");
                response.setHeader("Content-Disposition", "inline; filename=\"report.pptx\"");
                exporter = new JRPptxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
                break;
            case DOCX:
                response.setContentType("application/docx");
                response.setHeader("Content-Disposition", "inline; filename=\"report.docx\"");
                exporter = new JRDocxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
                break;
            case ODS:
                response.setContentType("application/ods");
                response.setHeader("Content-Disposition", "inline; filename=\"report.ods\"");
                exporter = new JROdsExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
                SimpleOdsReportConfiguration configurationOds = new SimpleOdsReportConfiguration();
                configurationOds.setOnePagePerSheet(true);
                exporter.setConfiguration(configurationOds);
                break;
            case ODT:
                response.setContentType("application/odt");
                response.setHeader("Content-Disposition", "inline; filename=\"report.odt\"");
                exporter = new JROdtExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
                break;

        }
        exporter.exportReport();
        Optional.ofNullable(response.getOutputStream()).ifPresent(outputStream -> {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
