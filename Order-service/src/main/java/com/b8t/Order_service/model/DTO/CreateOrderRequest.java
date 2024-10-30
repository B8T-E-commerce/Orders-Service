package com.b8t.Order_service.model.DTO;

import com.b8t.Order_service.model.OrderItem;
import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
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
    @NonNull
    private Long userId;  // ID of the user placing the order

    @NotBlank(message = "Total price is mandatory")
    private Double totalPrice;  // Total price of the order

    @NotBlank(message = "Status is mandatory")
    private String status;  // Order status, e.g., "pending", "processed", "sent"

    // Shipping address information
    private CreateShippingAddressesRequest shippingAddresses;

    // List of items in the order
    private Set<OrderItem> orderedItems;
}
