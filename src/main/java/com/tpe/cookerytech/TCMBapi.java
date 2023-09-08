package com.tpe.cookerytech;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.text.DecimalFormat;


@Component
public class TCMBapi{

//    public static void main(String[]args){
//        TCMBapi tcmBapi = new TCMBapi(WebClient.builder());
//        System.out.println(tcmBapi.getExchangeRate());
//    }
    private final WebClient webClient;

    public TCMBapi(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.tcmb.gov.tr").build();
    }

    public double getExchangeRate(String code) {
        int index=1;
        if (code.equals("TRY")) index =0;
        if (code.equals("USD")) return 1;
        if (code.equals("EUR")) {

            double tl = getExchangeRate("TRY");
            String xmlData = webClient.get()
                    .uri("/kurlar/today.xml")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Bloklayarak veriyi al, bu sadece örnek amaçlı kullanılmıştır.

            if (xmlData != null) {
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(new InputSource(new StringReader(xmlData)));

                    NodeList nodeList = doc.getElementsByTagName("BanknoteSelling");
                    if (nodeList.getLength() > 0) {
                        Element element = (Element) nodeList.item(3);
                        String exchangeRateString = element.getTextContent();
                        double result =tl/Double.parseDouble(exchangeRateString);

                        result = Math.floor(result * 10000 )/ 10000;

                        return result;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        String xmlData = webClient.get()
                .uri("/kurlar/today.xml")
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Bloklayarak veriyi al, bu sadece örnek amaçlı kullanılmıştır.

        if (xmlData != null) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(xmlData)));

                NodeList nodeList = doc.getElementsByTagName("BanknoteSelling");
                if (nodeList.getLength() > 0) {
                    Element element = (Element) nodeList.item(index);
                    String exchangeRateString = element.getTextContent();
                    return Double.parseDouble(exchangeRateString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 0.0; // Hata durumunda veya veri bulunamadığında varsayılan değer
    }
}
