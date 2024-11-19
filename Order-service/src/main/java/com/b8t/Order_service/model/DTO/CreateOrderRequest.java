package com.b8t.Order_service.model.DTO;

import com.b8t.Order_service.model.OrderItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull
    private Long userId;  // ID of the user placing the order

    @NotBlank(message = "Total price is mandatory")
    private Double totalPrice;  // Total price of the order

    @NotBlank(message = "Status is mandatory")
    private String status;  // Order status, e.g., "pending", "processed", "sent"

    @NotNull
    // Shipping address information
    private CreateShippingAddressesRequest shippingAddresses;

    @NotNull
    // List of items in the order
    private Set<OrderItem> orderedItems;
}
