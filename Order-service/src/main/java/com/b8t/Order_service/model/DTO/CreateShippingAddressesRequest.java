package com.b8t.Order_service.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShippingAddressesRequest {

    @NonNull
    @NotNull
    private String address1;

    private String address2;

    @NonNull
    @NotNull
    private String city;

    @NonNull
    @NotNull
    private String state;

    @NonNull
    @NotNull
    private String zip;

    @NonNull
    @NotNull
    private String country;

    @NonNull
    @NotNull
    private String email;  // Contact email associated with the shipping address
}
