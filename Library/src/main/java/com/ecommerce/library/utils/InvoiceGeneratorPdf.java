package com.ecommerce.library.utils;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.OrderDetails;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceGeneratorPdf {

    private Order order;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Add site name
        Font siteNameFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        siteNameFont.setSize(20);
        Paragraph siteNameParagraph = new Paragraph("ShoeZilla", siteNameFont);
        siteNameParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(siteNameParagraph);

        // Add user details
        Font invoiceFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        invoiceFont.setSize(14);
        Paragraph userDetailsParagraph = new Paragraph("Invoice", invoiceFont);
        userDetailsParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(userDetailsParagraph);

        document.add(new Paragraph("Name: " + order.getCustomer().getName()));
        document.add(new Paragraph("Email: " + order.getCustomer().getEmail()));
        document.add(new Paragraph("Phone: " + order.getCustomer().getMobile()));
        document.add(new Paragraph("Payment Method: " + order.getPaymentMethod()));

        // Add order details
        Font orderDetailsFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        orderDetailsFont.setSize(14);
        Paragraph orderDetailsParagraph = new Paragraph("Order Details", orderDetailsFont);
        orderDetailsParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(orderDetailsParagraph);

        // Add product details
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{2, 2, 2, 2});
        table.setSpacingBefore(10);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("Product Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);

        double totalPrice = 0;

        for (OrderDetails orderDetails : order.getOrderDetailList()) {
            table.addCell(orderDetails.getProduct().getName());
            table.addCell(orderDetails.getProduct().getDescription());
            table.addCell(String.valueOf(orderDetails.getQuantity()));
            double price = orderDetails.getProduct().getSalePrice();
            table.addCell(String.valueOf(price));
            totalPrice += price * orderDetails.getQuantity();
        }

        document.add(table);

        // Add total price
        Font totalPriceFont = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        totalPriceFont.setSize(14);
        Paragraph totalPriceParagraph = new Paragraph("Total Price: " + totalPrice, totalPriceFont);
        totalPriceParagraph.setAlignment(Paragraph.ALIGN_RIGHT);
        totalPriceParagraph.setSpacingBefore(10);
        document.add(totalPriceParagraph);

        // Add a short invoice description
        Paragraph invoiceDescription = new Paragraph("Thank you for your purchase! We hope you enjoy your new products. If you have any questions or concerns, please contact our support team.");
        invoiceDescription.setSpacingBefore(10);
        document.add(invoiceDescription);

        // Add signature
        Paragraph signature = new Paragraph("ShoeZilla", siteNameFont);
        signature.setAlignment(Paragraph.ALIGN_RIGHT);
        signature.setSpacingBefore(20);
        document.add(signature);

        document.close();
    }

    private String formatDateToDateString(LocalDate localDate) {
        return localDate.toString();  // Assumes the default format of "yyyy-MM-dd"
    }
}
