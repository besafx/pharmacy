package com.besafx.app.excel;

import com.besafx.app.entity.DetectionType;
import com.besafx.app.entity.Person;
import com.besafx.app.service.DetectionTypeService;
import com.besafx.app.service.PersonService;
import com.besafx.app.util.JSONConverter;
import com.besafx.app.util.Options;
import com.besafx.app.ws.Notification;
import com.besafx.app.ws.NotificationService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class ExcelDetectionTypeController {

    private final static Logger log = LoggerFactory.getLogger(ExcelDetectionTypeController.class);

    private SecureRandom random;

    @Autowired
    private PersonService personService;

    @Autowired
    private DetectionTypeService detectionTypeService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ExcelCellHelper excelCellHelper;

    @PostConstruct
    public void init() {
        random = new SecureRandom();
    }

    @RequestMapping(value = "/api/heavy-work/detectionType/write/{rowCount}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void writeExcelFile(@PathVariable(value = "rowCount") Integer rowCount, HttpServletResponse response, Principal principal) {
        log.info("فحص المستخدم");
        Person person = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(person.getOptions(), Options.class).getLang();
        //
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(person.getNickname() + " " + person.getName());
        Row row = sheet.createRow(0);
        row.setHeightInPoints((short) 25);
        //
        CreationHelper createHelper = workbook.getCreationHelper();
        //
        XSSFFont fontColumnHeader = workbook.createFont();
        fontColumnHeader.setBold(true);
        fontColumnHeader.setColor(IndexedColors.WHITE.getIndex());
        //
        XSSFFont fontCellDate = workbook.createFont();
        fontCellDate.setBold(true);
        fontCellDate.setColor(IndexedColors.BLACK.getIndex());
        //
        XSSFCellStyle styleColumnHeader = workbook.createCellStyle();
        styleColumnHeader.setFont(fontColumnHeader);
        styleColumnHeader.setAlignment(HorizontalAlignment.CENTER);
        styleColumnHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleColumnHeader.setBorderTop(BorderStyle.THIN);
        styleColumnHeader.setBorderLeft(BorderStyle.THIN);
        styleColumnHeader.setBorderBottom(BorderStyle.THIN);
        styleColumnHeader.setBorderRight(BorderStyle.THIN);
        styleColumnHeader.setFillForegroundColor(new XSSFColor(Color.decode("#795548")));
        styleColumnHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //
        XSSFCellStyle styleCellDate = workbook.createCellStyle();
        styleCellDate.setFont(fontCellDate);
        styleCellDate.setAlignment(HorizontalAlignment.CENTER);
        styleCellDate.setVerticalAlignment(VerticalAlignment.CENTER);
        styleCellDate.setBorderTop(BorderStyle.THIN);
        styleCellDate.setBorderLeft(BorderStyle.THIN);
        styleCellDate.setBorderBottom(BorderStyle.THIN);
        styleCellDate.setBorderRight(BorderStyle.THIN);
        styleCellDate.setFillForegroundColor(new XSSFColor(Color.WHITE));
        styleCellDate.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
        cellStyle.setFont(fontCellDate);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFillForegroundColor(new XSSFColor(Color.WHITE));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //
        Cell cell = row.createCell(0);
        cell.setCellValue(lang.equals("AR") ? "رقم النوع" : "Code");
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(styleColumnHeader);
        sheet.setColumnWidth(0, 20 * 256);
        //
        cell = row.createCell(1);
        cell.setCellValue(lang.equals("AR") ? "الاسم عربي" : "Arabic Name");
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(styleColumnHeader);
        sheet.setColumnWidth(1, 20 * 256);
        //
        cell = row.createCell(2);
        cell.setCellValue(lang.equals("AR") ? "الاسم إنجليزي" : "English Name");
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(styleColumnHeader);
        sheet.setColumnWidth(2, 20 * 256);
        //
        cell = row.createCell(3);
        cell.setCellValue(lang.equals("AR") ? "الوصف عربي" : "Arabic Description");
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(styleColumnHeader);
        sheet.setColumnWidth(3, 20 * 256);
        //
        cell = row.createCell(4);
        cell.setCellValue(lang.equals("AR") ? "الوصف إنجليزي" : "English Description");
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(styleColumnHeader);
        sheet.setColumnWidth(4, 20 * 256);
        //
        cell = row.createCell(5);
        cell.setCellValue(lang.equals("AR") ? "التكلفة" : "Cost");
        cell.setCellType(CellType.STRING);
        cell.setCellStyle(styleColumnHeader);
        sheet.setColumnWidth(5, 20 * 256);
        //
        for (int i = 1; i <= rowCount; i++) {
            row = sheet.createRow(i);
            row.setHeightInPoints((short) 25);
            //
            for (int j = 0; j <= 5; j++) {
                cell = row.createCell(j);
                cell.setCellType(CellType.STRING);
                cell.setCellValue("---");
                cell.setCellStyle(styleCellDate);
            }
        }
        //
        try {
            response.setContentType("application/xlsx");
            response.setHeader("Content-Disposition", "attachment; filename=\"PHARMACY4FALCON-HeavyWork-DetectionType-Insert.xlsx\"");
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }

    @RequestMapping(value = "/api/heavy-work/detectionType/read", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void readExcelFile(@RequestParam("file") MultipartFile multipartFile, Principal principal) {
        Person person = personService.findByEmail(principal.getName());
        String lang = JSONConverter.toObject(person.getOptions(), Options.class).getLang();
        try {
            List<DetectionType> detectionTypeList = new ArrayList<>();
            //
            File tempFile = File.createTempFile(new BigInteger(130, random).toString(32), "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
            FileUtils.writeByteArrayToFile(tempFile, multipartFile.getBytes());
            FileInputStream fileInputStream = new FileInputStream(tempFile);
            //
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet dataTypeSheet = workbook.getSheetAt(0);
            //
            Iterator<Row> iterator = dataTypeSheet.iterator();
            log.info("تجاهل الصف الأول من الجدول والذي يحتوي على رؤوس الأعمدة");
            if (iterator.hasNext()) iterator.next();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                DetectionType detectionType = new DetectionType();
                boolean accept = true;
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            if (excelCellHelper.getCellValue(nextCell) == null) {
                                accept = false;
                            }
                            nextCell.setCellType(CellType.STRING);
                            Integer code;
                            try {
                                code = Integer.parseInt((String) excelCellHelper.getCellValue(nextCell));
                            } catch (Exception ex) {
                                accept = false;
                                break;
                            }
                            detectionType.setCode(code);
                            log.info((String) excelCellHelper.getCellValue(nextCell));
                            break;
                        case 1:
                            if (excelCellHelper.getCellValue(nextCell) == null) {
                                accept = false;
                            }
                            nextCell.setCellType(CellType.STRING);
                            detectionType.setNameArabic((String) excelCellHelper.getCellValue(nextCell));
                            log.info((String) excelCellHelper.getCellValue(nextCell));
                            break;
                        case 2:
                            if (excelCellHelper.getCellValue(nextCell) == null) {
                                accept = false;
                            }
                            nextCell.setCellType(CellType.STRING);
                            detectionType.setNameEnglish((String) excelCellHelper.getCellValue(nextCell));
                            log.info((String) excelCellHelper.getCellValue(nextCell));
                            break;
                        case 3:
                            if (excelCellHelper.getCellValue(nextCell) == null) {
                                accept = false;
                            }
                            nextCell.setCellType(CellType.STRING);
                            detectionType.setDescriptionArabic((String) excelCellHelper.getCellValue(nextCell));
                            log.info((String) excelCellHelper.getCellValue(nextCell));
                            break;
                        case 4:
                            if (excelCellHelper.getCellValue(nextCell) == null) {
                                accept = false;
                            }
                            nextCell.setCellType(CellType.STRING);
                            detectionType.setDescriptionEnglish((String) excelCellHelper.getCellValue(nextCell));
                            log.info((String) excelCellHelper.getCellValue(nextCell));
                            break;
                        case 5:
                            if (excelCellHelper.getCellValue(nextCell) == null) {
                                accept = false;
                            }
                            nextCell.setCellType(CellType.STRING);
                            Double cost;
                            try {
                                cost = Double.parseDouble((String) excelCellHelper.getCellValue(nextCell));
                            } catch (Exception ex) {
                                accept = false;
                                break;
                            }
                            detectionType.setCost(cost);
                            log.info((String) excelCellHelper.getCellValue(nextCell));
                            break;
                    }

                }
                if (accept) {
                    try {
                        DetectionType foundDetectionType = detectionTypeService.findByCode(detectionType.getCode());
                        if (foundDetectionType != null) {
                            DetectionType topDetectionType = detectionTypeService.findTopByOrderByCodeDesc();
                            if (topDetectionType == null) {
                                detectionType.setCode(1);
                            } else {
                                detectionType.setCode(topDetectionType.getCode() + 1);
                            }
                        }
                        detectionTypeList.add(detectionType);
                        detectionTypeService.save(detectionType);
                    } catch (Exception ex) {
                        log.info(ex.getMessage());
                    }
                }
            }
            notificationService.notifyOne(Notification
                    .builder()
                    .title(lang.equals("AR") ? "مركز السلطان للصقور" : "SULTAN CENTER")
                    .message(lang.equals("AR") ? "تم اضافة عدد " + detectionTypeList.size() + " من الفحوصات بنجاح." : "Create " + detectionTypeList.size() + " Of Detection Types" + "Successfully")
                    .type("information")
                    .icon("fa-plus-square")
                    .layout(lang.equals("AR") ? "topLeft" : "topRight")
                    .build(), principal.getName());
            workbook.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
