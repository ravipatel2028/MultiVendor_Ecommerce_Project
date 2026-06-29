package com.ravi.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    private String title;
    private String description;
    private String category;
    private double mrpPrice;
    private double sellingPrice;
    private String color;
    private List<String> images;
    private String category2;
    private String category3;
    private String size;

}
