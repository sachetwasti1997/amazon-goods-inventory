package com.sachet.goodsinventoryamazon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderCreatedEventModal {
    private String orderId;
    private String status;
    private String expiresAt;
    private String itemId;
    private Integer orderQuantity;
    private Double orderPrice;
}
