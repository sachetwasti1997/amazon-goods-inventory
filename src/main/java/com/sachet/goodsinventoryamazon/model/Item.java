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
    private String productName;
    private String productDescription;
    private Integer price;
    private Integer userId;
    private String orderId;
    private String category;
    private List<String> imageURL;


}
