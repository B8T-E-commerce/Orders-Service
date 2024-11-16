package com.b8t.Order_service.model.DAL;

import com.b8t.Order_service.model.OrderItem;
import com.b8t.Order_service.model.ShippingAddresses;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
public class OrderDAL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private String status;          //  status:pending/processed/sent

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // One-to-One relationship with ShippingAddresses
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id")
    private ShippingAddresses shippingAddresses;

    // One-to-Many relationship with OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Prevents infinite recursion in JSON serialization
    private Set<OrderItem> orderedItems = new HashSet<>();

    // Helper method to add OrderItems
    public void addOrderItem(OrderItem item) {
        if (orderedItems == null) {
            orderedItems = new HashSet<>(); // Extra safety check
        }
        orderedItems.add(item);
        item.setOrder(this);  // Set the reference in the child entity
    }

    // Only use id to avoid circular references
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDAL orderDAL = (OrderDAL) o;
        return Objects.equals(id, orderDAL.id);
    }
}
