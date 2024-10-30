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

    @NonNull
    @NotNull
    private Long productId;

    @NonNull
    @NotNull
    private String productName;

    @NonNull
    @NotNull
    private Long sellerId;

    @NonNull
    @NotNull
    private int quantity;

    @NonNull
    private Double price;  // Price per unit of the product

}
