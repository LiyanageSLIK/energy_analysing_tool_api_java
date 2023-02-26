package com.greenbill.greenbill.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ReportGeneratorService {
    public ByteArrayOutputStream generateReport() throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // create a new PDF document
        Document document = new Document(PageSize.A4.rotate());

        // create a PDF writer that writes to the ByteArrayOutputStream
        PdfWriter.getInstance(document, outputStream);

        // open the document
        document.open();

        // add a title to the document
        Paragraph title = new Paragraph("My Report", FontFactory.getFont(FontFactory.TIMES_BOLD, 24));
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        // create a table to display the data
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // add table headers
        PdfPCell cell = new PdfPCell(new Paragraph("ID"));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Name"));
        table.addCell(cell);
        cell = new PdfPCell(new Paragraph("Value"));
        table.addCell(cell);

        // add table data
//        for (MyEntity entity : entities) {
//            table.addCell(String.valueOf(entity.getId()));
//            table.addCell(entity.getName());
//            table.addCell(String.valueOf(entity.getValue()));
//        }

        document.add(table);

        // close the document
        document.close();

        return outputStream;
    }
}
