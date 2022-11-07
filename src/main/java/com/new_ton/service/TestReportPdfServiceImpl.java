package com.new_ton.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lider.domain.dto.PrintTestReportDto;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class TestReportPdfServiceImpl implements TestReportPdfService {
    private static final Logger log = LoggerFactory.getLogger(TestReportPdfServiceImpl.class);
    private final PrintDocumentService printDocumentService;

    public TestReportPdfServiceImpl(PrintDocumentService printDocumentService) {
        this.printDocumentService = printDocumentService;
    }

    public boolean createTestReportPdf(PrintTestReportDto dto) {
        try {
            StringBuilder mainDirectory = new StringBuilder();
            mainDirectory.append("C:/NewTon");
            if (!Files.exists(Paths.get(mainDirectory.toString()), new LinkOption[0])) {
                log.info("Create directory " + mainDirectory);
                Files.createDirectory(Paths.get(mainDirectory.toString()));
            }

            mainDirectory.append("/TestReportLabels");
            if (!Files.exists(Paths.get(mainDirectory.toString()), new LinkOption[0])) {
                Files.createDirectory(Paths.get(mainDirectory.toString()));
                log.info("Create directory " + mainDirectory);
            }

            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(date);
            mainDirectory.append("/").append(currentDate);
            if (!Files.exists(Paths.get(mainDirectory.toString()), new LinkOption[0])) {
                Files.createDirectory(Paths.get(mainDirectory.toString()));
                log.info("Create directory " + mainDirectory);
            }

            log.info("Start create document " + mainDirectory);
            mainDirectory.append("/TestReportLabel_id_prod_").append(dto.getIdProd()).append(".pdf");
            File file = new File(mainDirectory.toString());
            if (file.exists()) {
                file.delete();
                log.info("Delete directory " + mainDirectory);
            }

            BaseColor color = new BaseColor(250, 193, 148);
            URL FONT = this.getClass().getClassLoader().getResource("fonts/RobotoCondensed-Regular.ttf");
            BaseFont baseFont = BaseFont.createFont(FONT.toString(), "Identity-H", true);
            Font font1 = new Font(baseFont, 10.0F, 0);
            Font font2 = new Font(baseFont, 9.0F, 0);
            Font font3 = new Font(baseFont, 8.0F, 1);
            URL imageFile = this.getClass().getClassLoader().getResource("image/new_ton.png");
            Image image = Image.getInstance(imageFile.toString());
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(95.0F);
            PdfPCell cell = new PdfPCell();
            cell.setRowspan(2);
            cell.setBorderWidth(0.6F);
            cell.setImage(image);
            cell.setFixedHeight(20.0F);
            cell.setHorizontalAlignment(1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Протокол випробувань (аналізів)", font1));
            cell.setColspan(4);
            cell.setHorizontalAlignment(1);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Код форми", font2));
            cell.setRowspan(2);
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBackgroundColor(color);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Ф К5 - 13", font3));
            cell.setVerticalAlignment(5);
            cell.setRowspan(2);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("№", font2));
            cell.setHorizontalAlignment(1);
            cell.setBackgroundColor(color);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(String.valueOf(dto.getNumbprot()), font2));
            cell.setHorizontalAlignment(1);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("від", font2));
            cell.setHorizontalAlignment(1);
            cell.setBackgroundColor(color);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getDateprot(), font2));
            cell.setHorizontalAlignment(1);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Торгова марка ", font2));
            cell.setColspan(2);
            cell.setBackgroundColor(color);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getBrend(), font3));
            cell.setColspan(5);
            cell.setHorizontalAlignment(1);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Найменування продукції", font2));
            cell.setColspan(2);
            cell.setBackgroundColor(color);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getNameprod(), font2));
            cell.setColspan(5);
            cell.setHorizontalAlignment(1);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Дата виготовлення", font2));
            cell.setColspan(2);
            cell.setBackgroundColor(color);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getDatemade(), font2));
            cell.setColspan(5);
            cell.setHorizontalAlignment(1);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Партія №", font2));
            cell.setColspan(2);
            cell.setBackgroundColor(color);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getNumbpart(), font2));
            cell.setColspan(5);
            cell.setHorizontalAlignment(1);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Кінцевий термін придатності", font2));
            cell.setColspan(2);
            cell.setBackgroundColor(color);
            cell.setFixedHeight(20.0F);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getExpdate(), font2));
            cell.setColspan(5);
            cell.setHorizontalAlignment(1);
            cell.setFixedHeight(20.0F);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table.addCell(cell);
            PdfPTable table1 = new PdfPTable(new float[]{0.45F, 1.55F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F});
            table1.setWidthPercentage(95.0F);
            cell = new PdfPCell(new Phrase("№ п/п", font2));
            cell.setHorizontalAlignment(1);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Показники", font2));
            cell.setHorizontalAlignment(1);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("НД", font2));
            cell.setHorizontalAlignment(1);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Допустиме значення", font2));
            cell.setHorizontalAlignment(1);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Похибка", font2));
            cell.setHorizontalAlignment(1);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Результат", font2));
            cell.setHorizontalAlignment(1);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("1", font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Зовнішній вигляд", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getAppearanceDto().getNd(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getAppearanceDto().getAllvalues(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getAppearanceDto().getDev(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getAppearanceDto().getResult(), font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("2", font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("В'язкість, с", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getViscosityDto().getNd(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getViscosityDto().getAllvalues(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("± " + dto.getViscosityDto().getDev() + " c", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getViscosityDto().getResult(), font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("3", font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Щільність г/см3 ", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getDensityDto().getNd(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getDensityDto().getAllvalues() + " г/см3", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("± " + dto.getDensityDto().getDev() + " гр.", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getDensityDto().getResult(), font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("4", font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Розведення, л", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("не регламується", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setRowspan(4);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("± " + dto.getDulitionDto().getDev() + " гр.", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            String dilutionFull = dto.getDulitionDto().getResult();
            int index = dilutionFull.indexOf("-");
            String dilution1 = dilutionFull.substring(0, index);
            String dilution2 = dilutionFull.substring(index + 1);
            cell = new PdfPCell(new Phrase(dilution1, font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dilution2, font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("5", font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Кількість газу, г", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("± " + dto.getQuantityDto().getDev() + " гр.", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getQuantityDto().getResult(), font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("6", font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Тип клапану", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getValveTypeDto().getResult(), font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("7", font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Тип розпилювальної голівки", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getSprayHeadTypeDto().getResult(), font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("8", font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Ступінь глянцю", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getDegreeOfGlossDto().getNd(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getDegreeOfGlossDto().getAllvalues(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getDegreeOfGlossDto().getResult(), font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("9", font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Колір", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getColorDto().getNd(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getColorDto().getAllvalues(), font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("", font2));
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getColorDto().getResult(), font2));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("                     "));
            cell.setBorderWidth(0.6F);
            cell.setColspan(7);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Висновок:", font3));
            cell.setBorderWidth(0.6F);
            cell.setColspan(7);
            cell.setVerticalAlignment(5);
            cell.setBackgroundColor(color);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getNameprod() + "                  допущенна(ний) до зливу                  " + dto.getFiltr(), font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(7);
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            table1.addCell(cell);
            cell = new PdfPCell(new Phrase("Протокол видав", font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(7);
            cell.setBackgroundColor(color);
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            table1.addCell(cell);
            PdfPTable table2 = new PdfPTable(new float[]{0.45F, 1.0F, 3.0F, 1.0F, 1.0F, 1.0F, 1.0F});
            table2.setWidthPercentage(95.0F);
            cell = new PdfPCell(new Phrase("Посада", font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("Начальник лабораторії з контролю виробництва", font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(5);
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("Підпис", font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("", font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(5);
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("П.І.Б", font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getLabfio(), font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(5);
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase("Дата", font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(2);
            cell.setBackgroundColor(color);
            cell.setVerticalAlignment(5);
            table2.addCell(cell);
            cell = new PdfPCell(new Phrase(dto.getDateprot(), font2));
            cell.setBorderWidth(0.6F);
            cell.setColspan(5);
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(5);
            table2.addCell(cell);
            Document document = new Document();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            document.add(table);
            document.add(table1);
            document.add(table2);
            document.close();
            log.info("End create document " + mainDirectory);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(mainDirectory.toString());

                boolean var26;
                try {
                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                    var26 = this.printDocumentService.printDocument(mainDirectory.toString());
                } catch (Throwable var29) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable var28) {
                        var29.addSuppressed(var28);
                    }

                    throw var29;
                }

                fileOutputStream.close();
                return var26;
            } catch (Exception var30) {
                log.error("Error save document : {}, {}", ExceptionUtils.getMessage(var30), ExceptionUtils.getMessage(var30.getCause()));
            }
        } catch (Exception var31) {
            log.error("Error create test report pdf : {}, {}", ExceptionUtils.getMessage(var31), ExceptionUtils.getMessage(var31.getCause()));
        }

        return false;
    }
}