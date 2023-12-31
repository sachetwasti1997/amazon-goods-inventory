package com.sachet.goodsinventoryamazon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemPublish {

    private String itemId;
    private Double price;
    private String title;
    private int totalQuantity;

}
