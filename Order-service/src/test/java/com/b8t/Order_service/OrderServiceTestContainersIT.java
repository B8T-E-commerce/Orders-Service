//package com.b8t.Order_service.integration;
//
//import com.b8t.Order_service.business.OrderService;
//import com.b8t.Order_service.model.OrderDomain;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Testcontainers
//class OrderServiceTestContainersIT {
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
//    void createOrder_WithTestContainers_ShouldPersistOrder() {
//        // Arrange
//        OrderDomain orderDomain = new OrderDomain();
//        orderDomain.setUserId("test-user-123");
//        orderDomain.setTotalPrice(new BigDecimal("199.99"));
//        orderDomain.setStatus("NEW");
//
//        // Add shipping addresses
//        List<String> shippingAddresses = new ArrayList<>();
//        shippingAddresses.add("123 Test Street, Test City, 12345");
//        shippingAddresses.add("456 Another St, Other City, 67890");
//        orderDomain.setShippingAddresses(shippingAddresses);
//
//        // Add ordered items
//        List<OrderDomain.OrderedItem> orderedItems = new ArrayList<>();
//
//        OrderDomain.OrderedItem item1 = new OrderDomain.OrderedItem();
//        item1.setProductId("PROD-001");
//        item1.setQuantity(2);
//        item1.setPrice(new BigDecimal("49.99"));
//
//        OrderDomain.OrderedItem item2 = new OrderDomain.OrderedItem();
//        item2.setProductId("PROD-002");
//        item2.setQuantity(1);
//        item2.setPrice(new BigDecimal("100.01"));
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
//        assertEquals(orderDomain.getTotalPrice(), createdOrder.getTotalPrice(), "Total price should match");
//        assertEquals(orderDomain.getStatus(), createdOrder.getStatus(), "Status should match");
//
//        // Verify shipping addresses
//        assertNotNull(createdOrder.getShippingAddresses(), "Shipping addresses should not be null");
//        assertEquals(2, createdOrder.getShippingAddresses().size(), "Should have 2 shipping addresses");
//        assertTrue(createdOrder.getShippingAddresses().containsAll(shippingAddresses),
//                "All shipping addresses should be preserved");
//
//        // Verify ordered items
//        assertNotNull(createdOrder.getOrderedItems(), "Ordered items should not be null");
//        assertEquals(2, createdOrder.getOrderedItems().size(), "Should have 2 ordered items");
//
//        // Verify individual items
//        createdOrder.getOrderedItems().forEach(savedItem -> {
//            Optional<OrderDomain.OrderedItem> originalItem = orderedItems.stream()
//                    .filter(item -> item.getProductId().equals(savedItem.getProductId()))
//                    .findFirst();
//
//            assertTrue(originalItem.isPresent(), "Each saved item should match an original item");
//            assertEquals(originalItem.get().getQuantity(), savedItem.getQuantity(),
//                    "Item quantity should match");
//            assertEquals(originalItem.get().getPrice(), savedItem.getPrice(),
//                    "Item price should match");
//        });
//
//        // Verify timestamps are set
//        assertNotNull(createdOrder.getCreatedAt(), "Created timestamp should be set");
//        assertNotNull(createdOrder.getUpdatedAt(), "Updated timestamp should be set");
//
//        // Optional: Verify total price calculation
//        BigDecimal expectedTotal = orderedItems.stream()
//                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        assertEquals(expectedTotal, createdOrder.getTotalPrice(), "Total price should be calculated correctly");
//    }
//}