package com.sachet.goodsinventoryamazon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderExpiredModal {
    private String orderId;
    private String itemId;
    private Integer orderQuantity;
}
