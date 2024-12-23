package com.b8t.Order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Ensure compatibility

    private Long paymentId;

    private ShippingAddresses shippingAddresses;

    private Double totalPrice;

    private Long shopperId;

    private Set<OrderItem> products;
}
