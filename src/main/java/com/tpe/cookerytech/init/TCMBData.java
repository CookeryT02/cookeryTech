package com.tpe.cookerytech.init;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Component
public class TCMBData {

    private final WebClient webClient;

    public TCMBData(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.tcmb.gov.tr").build();
    }

    public double getExchangeRate() {

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

                NodeList nodeList = doc.getElementsByTagName("Banknote Selling");
                if (nodeList.getLength() > 0) {
                    Element element = (Element) nodeList.item(0);
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
