package com.tpe.cookerytech.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private int categories;
    private int brands;
    private int products;
    private int offers;
    private int customers;

    @Override
    public String toString() {
        return "{" +"\n" +
                "categories=" + categories + "\n" +
                "brands=" + brands + "\n" +
                "products=" + products + "\n" +
                "offers=" + offers + "\n" +
                "customers=" + customers +"\n" +
                '}';
    }
}
