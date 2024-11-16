package com.b8t.Order_service.model.DTO;

import com.b8t.Order_service.model.ShippingAddresses;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    private String productName;

    @NotNull
    private Long sellerId;

    @NotNull
    private int quantity;

    @NotNull
    private Double price;  // Price per unit of the product

}
