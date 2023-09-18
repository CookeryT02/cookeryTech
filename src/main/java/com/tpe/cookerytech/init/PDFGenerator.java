package com.tpe.cookerytech.init;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.mapper.ProductMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.netty.http.server.HttpServerResponse;

import javax.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PDFGenerator {

    @Autowired
    private  ProductMapper productMapper;

    private List<ProductResponse> productList;

    public void generate(HttpServletResponse response) throws DocumentException, IOException{

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(20);

        Paragraph paragraph = new Paragraph("List Of Products",fontTitle);

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

        cell.setPhrase(new Phrase("ID",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Product",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Offer",font));
        table.addCell(cell);

        for(ProductResponse product:productList){
            table.addCell(product.getTitle());

        }

        document.add(table);
        document.close();
    }

}
