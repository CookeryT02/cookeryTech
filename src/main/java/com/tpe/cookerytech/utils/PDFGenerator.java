package com.tpe.cookerytech.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.dto.response.ProductResponsePDF;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import com.tpe.cookerytech.mapper.ProductMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PDFGenerator {



    private List<ProductResponse> productList;

    private List<ProductResponsePDF> productResponsePDFS;


    public void generate(HttpServletResponse response,String title, int count,int a) throws DocumentException, IOException{

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(20);

        Paragraph paragraph = new Paragraph(title,fontTitle);

        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(5);

        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3,3,3,3,3});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setUseBorderPadding(true);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        cell.setPhrase(new Phrase("ID",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Product",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Brand",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Category",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Offer Count",font));
        table.addCell(cell);

        if (a==2){
            for (ProductResponsePDF productResponsePDF:productResponsePDFS){
                table.addCell(String.valueOf(productResponsePDF.getId()));
                table.addCell(productResponsePDF.getTitle());
                table.addCell(String.valueOf(productResponsePDF.getBrandId()));
                table.addCell(String.valueOf(productResponsePDF.getCategoryId()));
                table.addCell(String.valueOf(productResponsePDF.getCount()));
            }
        }
        if(a==1) {
            for(ProductResponse productResponse:productList){
                table.addCell(String.valueOf(productResponse.getId()));
                table.addCell(productResponse.getTitle());
                table.addCell(String.valueOf(productResponse.getBrandId()));
                table.addCell(String.valueOf(productResponse.getCategoryId()));
                table.addCell(String.valueOf(count));
            }
        }

        document.add(table);
        document.close();
    }

}
