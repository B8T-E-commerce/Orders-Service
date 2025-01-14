package com.b8t.Order_service.model;


import com.b8t.Order_service.model.DAL.OrderDAL;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="shipping_addresses")
public class ShippingAddresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String email;

    // Bidirectional one-to-one relationship with OrderDAL
    @ToString.Exclude  // Add this to exclude from toString()
    @OneToOne(mappedBy = "shippingAddresses")
    private OrderDAL order;

}
