package com.b8t.Order_service.model;

import com.b8t.Order_service.model.DAL.OrderDAL;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ordered_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private Long productId;
    private String productName;
    private Long sellerId;
    private Integer quantity;
    private Double price;
    // Many-to-One relationship with OrderDAL
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @ToString.Exclude  // Add this to exclude from toString()
    private OrderDAL order;

    // Override hashCode and equals to avoid conflicts in the Set before id is assigned
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(productId, orderItem.productId);
    }

}
