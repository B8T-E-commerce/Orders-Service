//package com.b8t.Order_service.integration;
//
//import com.b8t.Order_service.business.OrderService;
//import com.b8t.Order_service.model.DAL.OrderDAL;
//import com.b8t.Order_service.model.OrderDomain;
//import com.b8t.Order_service.model.OrderItem;
//import com.b8t.Order_service.model.ShippingAddresses;
//import com.b8t.Order_service.persistance.OrderRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Testcontainers
//class OrderServiceIntegrationTest {
//
//    @Container
//    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mysql::getJdbcUrl);
//        registry.add("spring.datasource.username", mysql::getUsername);
//        registry.add("spring.datasource.password", mysql::getPassword);
//    }
//
//    @Autowired
//    private OrderService orderService;
//
//    @Test
//    void createOrder_ShouldPersistOrder() {
//        // Arrange
//        OrderDomain orderDomain = new OrderDomain();
//        orderDomain.setUserId(1L); // Using Long instead of String
//        orderDomain.setTotalPrice(199.99); // Using double instead of BigDecimal
//        orderDomain.setStatus("NEW");
//
//        // Create shipping address
//        ShippingAddresses shippingAddress = new ShippingAddresses();
//        shippingAddress.setAddress1("123 Test Street");  // Change to address1
//        shippingAddress.setCity("Test City");
//        shippingAddress.setZip("12345");  // Change to zip
//        orderDomain.setShippingAddresses(shippingAddress);
//
//        // Create ordered items
//        Set<OrderItem> orderedItems = new HashSet<>();
//
//        OrderItem item1 = new OrderItem();
//        item1.setProductId(1L);
//        item1.setQuantity(2);
//        item1.setPrice(49.99);
//
//        OrderItem item2 = new OrderItem();
//        item2.setProductId(2L);
//        item2.setQuantity(1);
//        item2.setPrice(100.01);
//
//        orderedItems.add(item1);
//        orderedItems.add(item2);
//        orderDomain.setOrderedItems(orderedItems);
//
//        // Act
//        OrderDomain createdOrder = orderService.createOrder(orderDomain);
//
//        // Assert
//        assertNotNull(createdOrder, "Created order should not be null");
//        assertNotNull(createdOrder.getId(), "Created order should have an ID");
//
//        // Verify order details
//        assertEquals(orderDomain.getUserId(), createdOrder.getUserId(), "User ID should match");
//        assertEquals(orderDomain.getTotalPrice(), createdOrder.getTotalPrice(), 0.01, "Total price should match");
//        assertEquals(orderDomain.getStatus(), createdOrder.getStatus(), "Status should match");
//
//        // Verify shipping address
//        assertNotNull(createdOrder.getShippingAddresses(), "Shipping address should not be null");
//        assertEquals(shippingAddress.getAddress1(),  // Change to address1
//                createdOrder.getShippingAddresses().getAddress1(),  // Change to address1
//                "Shipping address should match");
//
//        // Verify ordered items
//        assertNotNull(createdOrder.getOrderedItems(), "Ordered items should not be null");
//        assertEquals(2, createdOrder.getOrderedItems().size(), "Should have 2 ordered items");
//
//        // Verify timestamps
//        assertNotNull(createdOrder.getCreatedAt(), "Created timestamp should be set");
//        assertNotNull(createdOrder.getUpdatedAt(), "Updated timestamp should be set");
//    }
//
//}