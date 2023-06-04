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

        Paragraph calculationDetails= new Paragraph();

        calculationDetails.setFont(bodyFont);
        calculationDetails.add(new Chunk("TOTAL UNITS \t",heading2Font));
        calculationDetails.add(new Chunk(":"+ String.valueOf(billDetails.getTotalUnits()) +"\n",bodyFont));
        calculationDetails.add(new Chunk("MONTHLY FIXED CHARGE \t",heading2Font));
        calculationDetails.add(new Chunk(":"+ String.valueOf(billDetails.getFixedCharge()) +"\n",bodyFont));
        for (Object line:billDetails.getCalculationSteps()) {
            calculationDetails.add(new Chunk(">"+ line.toString() +"\n",bodyFont));
        }
        calculationDetails.add(new Chunk("CHARGE FOR CONSUMPTION \t",heading2Font));
        calculationDetails.add(new Chunk(":"+ String.valueOf(billDetails.getUsageCharge()) +"\n",bodyFont));
        calculationDetails.add(new Chunk("CHARGE FOR THE MONTH \t",heading2Font));
        calculationDetails.add(new Chunk(":"+ String.valueOf(billDetails.getTotalCharge()) +"\n",bodyFont));
        calculationDetails.add(new Chunk("SSCL LEVY \t",heading2Font));
        calculationDetails.add(new Chunk(":"+ String.valueOf(billDetails.getLevy()) +"\n",bodyFont));
        calculationDetails.add(new Chunk("TOTAL BILL AMOUNT \t",heading2Font));
        calculationDetails.add(new Chunk(":"+ String.valueOf(billDetails.getBillAmount()) +"\n",bodyFont));

        calculationDetails.setTabSettings(new TabSettings());

        document.add(calculationDetails);



        // create a table
//        PdfPTable table = new PdfPTable(3);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10f);
//        table.setSpacingAfter(10f);

        // add table headers
//        PdfPCell cell = new PdfPCell(new Paragraph("ID"));
//        table.addCell(cell);
//        cell = new PdfPCell(new Paragraph("Name"));
//        table.addCell(cell);
//        cell = new PdfPCell(new Paragraph("Value"));
//        table.addCell(cell);

        // add table data
        // for (MyEntity entity : entities) {
        //     table.addCell(String.valueOf(entity.getId()));
        //     table.addCell(entity.getName());
        //     table.addCell(String.valueOf(entity.getValue()));
        // }

//        document.add(table);

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
