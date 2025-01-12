package com.b8t.Order_service.rabbitMQ;

import com.b8t.Order_service.business.OrderService;
import com.b8t.Order_service.model.DTO.CreateOrderRequest;
import com.b8t.Order_service.model.DTO.CreateOrderResponse;
import com.b8t.Order_service.model.DTO.CreateShippingAddressesRequest;
import com.b8t.Order_service.model.OrderDomain;
import com.b8t.Order_service.model.OrderItem;
import com.b8t.Order_service.model.Payment;
import com.b8t.Order_service.model.ShippingAddresses;
import com.b8t.Order_service.utilities.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RabbitMQTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderService orderService;

    private OrdersListener ordersListener;

    private Payment testPayment;
    private OrderDomain testOrder;
    private CreateOrderResponse testCreateOrderResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ordersListener = new OrdersListener(orderMapper, orderService);
        setupTestData();
    }

    private void setupTestData() {
        // Create test OrderItem
        OrderItem orderItem = OrderItem.builder()
                .productId(1L)
                .productName("Test Product")
                .sellerId(1L)
                .price(100.0)
                .quantity(1)
                .build();

        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem);

        // Create test ShippingAddresses
        ShippingAddresses shippingAddresses = ShippingAddresses.builder()
                .address1("123 Test St")
                .address2("Apt 4")
                .city("Test City")
                .state("Test State")
                .country("Test Country")
                .zip("12345")
                .email("test@test.com")
                .build();

        // Create test Payment
        testPayment = Payment.builder()
                .shopperId(1L)
                .totalPrice(100.0)
                .products(orderItems)
                .shippingAddresses(shippingAddresses)
                .build();

        // Create test CreateOrderResponse
        testCreateOrderResponse = CreateOrderResponse.builder()
                .orderId(1L)
                .build();
    }

    @Test
    void receivePaymentAndConvertToOrder_ShouldProcessPaymentCorrectly() {
        // Arrange
        when(orderMapper.convertCreateOrderRequestToDomain(any())).thenReturn(testOrder);
        when(orderService.createOrder(any())).thenReturn(testOrder);
        when(orderMapper.convertDomainToCreateOrderResponse(any())).thenReturn(testCreateOrderResponse);

        // Act
        ordersListener.receivePaymentAndConvertToOrder(testPayment);

        // Assert
        verify(orderService, times(1)).createOrder(any());
        verify(orderMapper, times(1)).convertCreateOrderRequestToDomain(any());
        verify(orderMapper, times(1)).convertDomainToCreateOrderResponse(any());
    }

    @Test
    void mapPaymentShippingAddressesToCreateShippingAddressesRequest_ShouldMapCorrectly() {
        // Act
        CreateShippingAddressesRequest result = ordersListener.mapPaymentShippingAddressesToCreateShippingAddressesRequest(testPayment);

        // Assert
        assertNotNull(result);
        assertEquals(testPayment.getShippingAddresses().getAddress1(), result.getAddress1());
        assertEquals(testPayment.getShippingAddresses().getAddress2(), result.getAddress2());
        assertEquals(testPayment.getShippingAddresses().getCity(), result.getCity());
        assertEquals(testPayment.getShippingAddresses().getState(), result.getState());
        assertEquals(testPayment.getShippingAddresses().getCountry(), result.getCountry());
        assertEquals(testPayment.getShippingAddresses().getZip(), result.getZip());
        assertEquals(testPayment.getShippingAddresses().getEmail(), result.getEmail());
    }

    @Test
    void mapPaymentProductsToOrderItems_ShouldMapCorrectly() {
        // Act
        Set<OrderItem> result = ordersListener.mapPaymentProductsToOrderItems(testPayment);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        OrderItem resultItem = result.iterator().next();
        OrderItem originalItem = testPayment.getProducts().iterator().next();

        assertEquals(originalItem.getProductId(), resultItem.getProductId());
        assertEquals(originalItem.getProductName(), resultItem.getProductName());
        assertEquals(originalItem.getSellerId(), resultItem.getSellerId());
        assertEquals(originalItem.getPrice(), resultItem.getPrice());
        assertEquals(originalItem.getQuantity(), resultItem.getQuantity());
    }

    @Test
    void mapPaymentProductsToOrderItems_WithNullPayment_ShouldReturnEmptySet() {
        // Act
        Set<OrderItem> result = ordersListener.mapPaymentProductsToOrderItems(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void mapPaymentProductsToOrderItems_WithNullProducts_ShouldReturnEmptySet() {
        // Arrange
        Payment paymentWithNullProducts = Payment.builder()
                .shopperId(1L)
                .totalPrice(100.0)
                .products(null)
                .build();

        // Act
        Set<OrderItem> result = ordersListener.mapPaymentProductsToOrderItems(paymentWithNullProducts);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void buildCreateOrderRequest_ShouldBuildCorrectly() {
        // Act
        CreateOrderRequest result = ordersListener.buildCreateOrderRequest(testPayment);

        // Assert
        assertNotNull(result);
        assertEquals(testPayment.getShopperId(), result.getUserId());
        assertEquals(testPayment.getTotalPrice(), result.getTotalPrice());
        assertEquals("pending", result.getStatus());
        assertNotNull(result.getShippingAddresses());
        assertNotNull(result.getOrderedItems());
        assertFalse(result.getOrderedItems().isEmpty());
    }
}