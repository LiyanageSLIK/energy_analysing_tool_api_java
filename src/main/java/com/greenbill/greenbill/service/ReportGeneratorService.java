package com.greenbill.greenbill.service;

import com.greenbill.greenbill.dto.response.CalculatedBillDto;
import com.greenbill.greenbill.entity.ProjectEntity;
import com.greenbill.greenbill.entity.UserEntity;
import com.greenbill.greenbill.repository.ProjectRepository;
import com.greenbill.greenbill.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class ReportGeneratorService {

    @Autowired
    private PlayGroundService playGroundService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ByteArrayOutputStream generateReport(long projectId,String email) throws DocumentException, IOException, HttpClientErrorException {

        UserEntity user=userRepository.findByEmail(email);
        ProjectEntity project=projectRepository.findBySubscription_User_EmailAndId(email,projectId);
        CalculatedBillDto billDetails=playGroundService.calculateBill(projectId);
        if(project==null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"Project Not found related to the user");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // create a new PDF document
        Document document = new Document(PageSize.A4);
        // create a PDF writer that writes to the ByteArrayOutputStream
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        // create a header event handler to add the logo
        HeaderEventHandler headerHandler = new HeaderEventHandler();
        writer.setPageEvent(headerHandler);
        // create a footer event handler to add the footer content
        FooterEventHandler footerHandler = new FooterEventHandler();
        writer.setPageEvent(footerHandler);
        // open the document
        document.open();
        // Load the template PDF
        PdfReader templateReader = new PdfReader("/templates/template.pdf");
        // Get the first page of the template PDF
        PdfTemplate template = writer.getImportedPage(templateReader,1);

        // Create a PdfContentByte object to add content to the document
        PdfContentByte contentByte = writer.getDirectContent();

        // Add the template to the document at position (0, 0)
        contentByte.addTemplate(template, 0, 0);
        //creating font styles
        Font heading1Font = new Font(FontFactory.getFont(FontFactory.TIMES_BOLD, 24,1));
        Font heading2Font = new Font(FontFactory.getFont(FontFactory.TIMES, 12,0,BaseColor.BLACK));
        Font bodyFont = new Font(Font.FontFamily.COURIER, 12);


        // create a title
        Paragraph emptyLine = new Paragraph(".", heading1Font);
        emptyLine.setSpacingAfter(20);
        Paragraph title = new Paragraph("Project Green Bill Report", heading1Font);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingBefore(10);
        title.setSpacingAfter(50);
        document.add(emptyLine);
        document.add(title);

        // create a paragraph
        Paragraph projectDetails = new Paragraph();
        projectDetails.add(new Chunk("ACCOUNT HOLDER \t",heading2Font));
        projectDetails.add(new Chunk(":"+user.getFullName().toString()+"\n",bodyFont));
        projectDetails.add(new Chunk("PROJECT NAME        \t",heading2Font));
        projectDetails.add(new Chunk(":"+project.getName().toString()+"\n",bodyFont));
        projectDetails.add(new Chunk("PROJECT TYPE          \t",heading2Font));
        projectDetails.add(new Chunk(":"+project.getProjectType().toString().toUpperCase()+"\n\n",bodyFont));
        projectDetails.add(new Chunk("PROJECT UPDATED AT    \t",heading2Font));
        projectDetails.add(new Chunk(":"+project.getLastUpdated().toString()+"\n",bodyFont));
        projectDetails.add(new Chunk("REPORT GENERATED AT\t",heading2Font));
        projectDetails.add(new Chunk(":"+new Date().toString()+"\n",bodyFont));
        projectDetails.setSpacingAfter(30);
        document.add(projectDetails);


        String currencyCode=billDetails.getFormat().getCurrency().getSymbol();

        // Create a table with 3 columns
        float relativeWidth[]={50.0F,5.0F,35.0F,10.0F};
        PdfPTable table1 = new PdfPTable(relativeWidth);
        table1.setWidthPercentage(65);
        table1.setSpacingBefore(10f);
        table1.setSpacingAfter(10f);
        table1.setHorizontalAlignment(0);

        // Create a PdfPCell for each chunk of calculation details
        PdfPCell cell1,cell2,cell3,cell4;

        cell1 = new PdfPCell(new Phrase("TOTAL UNITS", heading2Font));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2 = new PdfPCell(new Phrase(":", bodyFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3 = new PdfPCell(new Phrase(billDetails.getTotalUnits()+"_", bodyFont));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(2);
        cell4 = new PdfPCell(new Phrase("", bodyFont));
        cell4.setBorder(Rectangle.NO_BORDER);
        table1.addCell(cell1);
        table1.addCell(cell2);
        table1.addCell(cell3);
        table1.addCell(cell4);

        cell1 = new PdfPCell(new Phrase("MONTHLY FIXED CHARGE", heading2Font));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2 = new PdfPCell(new Phrase(":", bodyFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3 = new PdfPCell(new Phrase(billDetails.getFixedCharge().split(" ")[1], bodyFont));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(2);
        cell4 = new PdfPCell(new Phrase( currencyCode, bodyFont));
        cell4.setBorder(Rectangle.NO_BORDER);
        table1.addCell(cell1);
        table1.addCell(cell2);
        table1.addCell(cell3);
        table1.addCell(cell4);


        document.add(table1);

        // create a paragraph
        Paragraph calculationDetails= new Paragraph();
        calculationDetails.setFont(bodyFont);

        for (Object line:billDetails.getCalculationSteps()) {
            calculationDetails.add(new Chunk(">"+ line.toString() +"\n",bodyFont));
        }

        document.add(calculationDetails);

        // Create a table with 3 columns
        PdfPTable table2 = new PdfPTable(relativeWidth);
        table2.setWidthPercentage(65);
        table2.setSpacingBefore(10f);
        table2.setSpacingAfter(10f);
        table2.setHorizontalAlignment(0);

        cell1 = new PdfPCell(new Phrase("CHARGE FOR CONSUMPTION", heading2Font));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2 = new PdfPCell(new Phrase(":", bodyFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3 = new PdfPCell(new Phrase(billDetails.getUsageCharge().split(" ")[1], bodyFont));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(2);
        cell4 = new PdfPCell(new Phrase(currencyCode, bodyFont));
        cell4.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell1);
        table2.addCell(cell2);
        table2.addCell(cell3);
        table2.addCell(cell4);

        cell1 = new PdfPCell(new Phrase("CHARGE FOR THE MONTH", heading2Font));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2 = new PdfPCell(new Phrase(":", bodyFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3 = new PdfPCell(new Phrase(billDetails.getTotalCharge().split(" ")[1], bodyFont));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(2);
        cell4 = new PdfPCell(new Phrase(currencyCode, bodyFont));
        cell4.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell1);
        table2.addCell(cell2);
        table2.addCell(cell3);
        table2.addCell(cell4);

        cell1 = new PdfPCell(new Phrase("SSCL LEVY", heading2Font));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2 = new PdfPCell(new Phrase(":", bodyFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3 = new PdfPCell(new Phrase(billDetails.getLevy().split(" ")[1], bodyFont));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(2);
        cell4 = new PdfPCell(new Phrase(currencyCode, bodyFont));
        cell4.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell1);
        table2.addCell(cell2);
        table2.addCell(cell3);
        table2.addCell(cell4);

        cell1 = new PdfPCell(new Phrase("TOTAL BILL AMOUNT", heading2Font));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2 = new PdfPCell(new Phrase(":", bodyFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3 = new PdfPCell(new Phrase(billDetails.getBillAmount().split(" ")[1], bodyFont));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setHorizontalAlignment(2);
        cell4 = new PdfPCell(new Phrase(currencyCode, bodyFont));
        cell4.setBorder(Rectangle.NO_BORDER);
        table2.addCell(cell1);
        table2.addCell(cell2);
        table2.addCell(cell3);
        table2.addCell(cell4);

        document.add(table2);



        // close the document
        document.close();



        return outputStream;
    }

    class HeaderEventHandler extends PdfPageEventHelper {
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                // load the logo image
//                Image logo = Image.getInstance(getClass().getResource("/logo.png"));
//                logo.setAbsolutePosition(50, document.getPageSize().getHeight() - 60);
//                logo.scaleAbsolute(100, 100);


                // add the logo to the header
//                PdfContentByte contentByte = writer.getDirectContent();
//                contentByte.addImage(logo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class FooterEventHandler extends PdfPageEventHelper {
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                // create the footer paragraph
                Paragraph footer = new Paragraph("© 2023 Copyright: greenbill.lk", FontFactory.getFont(FontFactory.TIMES, 10,BaseColor.WHITE));
                footer.setAlignment(Element.ALIGN_CENTER);

                // add the footer to the document
                PdfContentByte contentByte = writer.getDirectContent();
                ColumnText.showTextAligned(contentByte, Element.ALIGN_CENTER, new Phrase(footer), (document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 16, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
