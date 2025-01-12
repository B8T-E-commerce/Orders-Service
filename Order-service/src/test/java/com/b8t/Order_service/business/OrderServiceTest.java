package com.b8t.Order_service.business;

import com.b8t.Order_service.model.DAL.OrderDAL;
import com.b8t.Order_service.model.OrderDomain;
import com.b8t.Order_service.persistance.OrderRepository;
import com.b8t.Order_service.utilities.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, orderMapper);
    }

    @Test
    void createOrder_ShouldSuccessfullyCreateOrder() {
        // Arrange
        OrderDomain inputOrderDomain = new OrderDomain();
        OrderDAL mappedOrderDAL = new OrderDAL();
        OrderDAL savedOrderDAL = new OrderDAL();
        OrderDomain expectedOrderDomain = new OrderDomain();

        when(orderMapper.convertDomainToOrderDAL(inputOrderDomain)).thenReturn(mappedOrderDAL);
        when(orderRepository.save(mappedOrderDAL)).thenReturn(savedOrderDAL);
        when(orderMapper.convertOrderDALToDomain(savedOrderDAL)).thenReturn(expectedOrderDomain);

        // Act
        OrderDomain result = orderService.createOrder(inputOrderDomain);

        // Assert
        assertNotNull(result);
        assertEquals(expectedOrderDomain, result);
        verify(orderMapper).convertDomainToOrderDAL(inputOrderDomain);
        verify(orderRepository).save(mappedOrderDAL);
        verify(orderMapper).convertOrderDALToDomain(savedOrderDAL);
    }

    @Test
    void createOrder_WhenMapperReturnsNull_ShouldHandleGracefully() {
        // Arrange
        OrderDomain inputOrderDomain = new OrderDomain();
        when(orderMapper.convertDomainToOrderDAL(inputOrderDomain)).thenReturn(null);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> orderService.createOrder(inputOrderDomain));
        assertEquals("Failed to map order domain to DAL", exception.getMessage());

        // Verify
        verify(orderMapper).convertDomainToOrderDAL(inputOrderDomain);
        verify(orderRepository, never()).save(any());
        verify(orderMapper, never()).convertOrderDALToDomain(any());
    }

    @Test
    void createOrder_WhenRepositorySaveFailsShouldThrowException() {
        // Arrange
        OrderDomain inputOrderDomain = new OrderDomain();
        OrderDAL mappedOrderDAL = new OrderDAL();
        when(orderMapper.convertDomainToOrderDAL(inputOrderDomain)).thenReturn(mappedOrderDAL);
        when(orderRepository.save(any())).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(inputOrderDomain);
        });
        verify(orderMapper).convertDomainToOrderDAL(inputOrderDomain);
        verify(orderRepository).save(mappedOrderDAL);
        verify(orderMapper, never()).convertOrderDALToDomain(any());
    }

    @Test
    void createOrder_WhenOrderDomainIsNull_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(null));
        assertEquals("OrderDomain cannot be null", exception.getMessage());

        // Verify no interactions with dependencies
        verifyNoInteractions(orderMapper, orderRepository);
    }
}