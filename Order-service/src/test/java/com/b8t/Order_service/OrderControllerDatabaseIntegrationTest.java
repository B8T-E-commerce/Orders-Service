//package com.b8t.Order_service;
//
//import com.b8t.Order_service.model.DTO.CreateOrderRequest;
//import com.b8t.Order_service.model.DTO.CreateShippingAddressesRequest;
//import com.b8t.Order_service.model.OrderItem;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.*;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//@ExtendWith(SpringExtension.class)
//class OrderControllerDatabaseIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final Set<OrderItem> mockOrderedItems = new HashSet<>(Arrays.asList(
//            OrderItem.builder()
//                    .productId(456L)
//                    .productName("Widget A")
//                    .sellerId(789L)
//                    .quantity(2)
//                    .price(29.99)
//                    .build(),
//            OrderItem.builder()
//                    .productId(457L)
//                    .productName("Widget B")
//                    .sellerId(790L)
//                    .quantity(1)
//                    .price(39.99)
//                    .build()
//    ));
//
//    private final CreateShippingAddressesRequest mockCreateShippingAddressesRequest = CreateShippingAddressesRequest.builder()
//            .address1("123 Main St")
//            .address2("Apt 4B")
//            .city("Springfield")
//            .state("IL")
//            .zip("62701")
//            .country("USA")
//            .email("user@example.com")
//            .build();
//
//    @Test
//    void testCreateOrderWithDatabase() throws Exception {
//        // Arrange
//        CreateOrderRequest request = CreateOrderRequest.builder()
//                .userId(123L)
//                .totalPrice(99.99)
//                .status("pending")
//                .shippingAddresses(mockCreateShippingAddressesRequest)
//                .orderedItems(mockOrderedItems)
//                .build();
//
//        // Act & Assert
//        mockMvc.perform(post("/orders")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.orderId").exists());  // Validate `orderId` exist
//    }
//
////    @Test
////    void testCreateOrderWithNullUserId() throws Exception {
////        // Create an invalid request by setting a negative totalPrice
////        CreateOrderRequest invalidRequest = CreateOrderRequest.builder()
////                // Missing userId or set to null
////                .totalPrice(99.99)
////                .status("pending")
////                .shippingAddresses(mockCreateShippingAddressesRequest)  // Assuming valid address
////                .orderedItems(mockOrderedItems)  // Assuming valid orderedItems
////                .build();
////        // !! Missing orderId
////
////        // Act & Assert: Send the invalid request and expect a 400 Bad Request response
////        mockMvc.perform(post("/orders")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(invalidRequest)))
////                .andExpect(status().isBadRequest());  // Expect 400 Bad Request due to invalid totalPrice
////    }
//
//}
//
