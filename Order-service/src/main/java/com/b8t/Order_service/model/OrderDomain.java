package com.b8t.Order_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDomain {

    private Long id;
    private Long userId;
    private Double totalPrice;
    private String status;          //  status:pending/processed/sent
    private Date createdAt;
    private Date updatedAt;
    private ShippingAddresses shippingAddresses;
    private Set<OrderItem> orderedItems;

}

