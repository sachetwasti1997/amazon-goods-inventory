package com.sachet.goodsinventoryamazon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {

    private String id;
    private String userId;
    private String productName;
    private String productDescription;
    private Double price;
    private String category;
    private int totalQuantity;
    private List<String> imageURL;

}
