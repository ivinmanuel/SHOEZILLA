package com.ecommerce.library.utils;

import com.ecommerce.library.model.Order;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfGeneratorAllInvoice {

    private List<Order> orders;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        titleFont.setSize(20);
        Paragraph title = new Paragraph("Order List", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        PdfPTable table = new PdfPTable(5); // Adjust the number of columns as needed
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1, 2, 2, 2, 2}); // Adjust column widths as needed
        table.setSpacingBefore(10);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);

        Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        headerFont.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("Order ID", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Date", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Status", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total Price", headerFont));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Payment Method", headerFont));
        table.addCell(cell);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Order order : orders) {
            table.addCell(String.valueOf(order.getId()));
            table.addCell(String.valueOf(order.getOrderDate()));
            table.addCell(order.getOrderStatus());
            table.addCell(String.valueOf(order.getGrandTotalPrize()));
            table.addCell(order.getPaymentMethod());
        }
        document.add(table);
        document.close();
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
