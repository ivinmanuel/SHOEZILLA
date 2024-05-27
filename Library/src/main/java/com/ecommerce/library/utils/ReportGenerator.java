package com.ecommerce.library.utils;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;


@Service
public class ReportGenerator {

    public String generateProductStatsPdf(List<Object[]> productStats, String from, String to) {
        String fileName = UUID.randomUUID().toString();
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/reports/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = uploadDir + fileName + ".pdf";
        Document document = new Document(PageSize.A1);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Paragraph title = new Paragraph("Product Stats Report");
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            Paragraph period = new Paragraph("From " + from + " to " + to);
            period.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(period);

            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(5);
            table.addCell("Product ID");
            table.addCell("Product Name");
            table.addCell("Category");
            table.addCell("Total Quantity Sold");
            table.addCell("Total Revenue");

            for (Object[] productStat : productStats) {
                table.addCell(productStat[0].toString());
                table.addCell(productStat[1].toString());
                table.addCell(String.valueOf(productStat[2]));
                table.addCell(String.valueOf(productStat[3]));
                table.addCell(String.valueOf(productStat[4]));
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return filePath;
    }

    public String generateProductStatsCsv(List<Object[]> productStats) {
        String fileName = UUID.randomUUID().toString();
        String rootPath = System.getProperty("user.dir");
        String uploadDir = rootPath + "/src/main/resources/static/reports/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = uploadDir + fileName + ".csv";
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("Product ID,Product Name,Category,Total Quantity Sold,Total Revenue");

            for (Object[] productStat : productStats) {
                writer.printf("%s,%s,%s,%s,%s%n",
                        productStat[0].toString(),
                        productStat[1].toString(),
                        productStat[2].toString(),
                        productStat[3].toString(),
                        productStat[4].toString());
            }

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}
