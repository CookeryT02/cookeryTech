package com.tpe.cookerytech.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PDFGeneratorReportSummery {

    private List<ReportOfferResponse> reportOfferResponseList;

    public void generate(HttpServletResponse response,String title, int count) throws DocumentException, IOException{

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(20);

        Paragraph paragraph = new Paragraph(title,fontTitle);

        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(3);

        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3,3,3});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setUseBorderPadding(true);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("Period",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total Product",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total Amount",font));
        table.addCell(cell);


        for (ReportOfferResponse reportOfferResponse: reportOfferResponseList){
                table.addCell(reportOfferResponse.getPeriod());
                table.addCell(String.valueOf(reportOfferResponse.getTotalProduct()));
                table.addCell(String.valueOf(reportOfferResponse.getTotalAmount()));
        }

        document.add(table);
        document.close();
    }

}
