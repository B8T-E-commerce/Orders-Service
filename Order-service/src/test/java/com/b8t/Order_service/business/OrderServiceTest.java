//package com.b8t.Order_service.business;
//
//import com.b8t.Order_service.model.DAL.OrderDAL;
//import com.b8t.Order_service.model.OrderDomain;
//import com.b8t.Order_service.persistance.OrderRepository;
//import com.b8t.Order_service.utilities.OrderMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.testcontainers.junit.jupiter.Testcontainers; // Corrected import statement
//
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@Testcontainers // Correctly applied Testcontainers annotation
//class OrderServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private OrderMapper orderMapper;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this); // Initialize mocks and inject them
//    }
//
//    @Test
//    void testCreateOrder_Success() {
//        // Arrange
//        OrderDomain orderDomain = new OrderDomain(); // Input for the service method
//        OrderDAL orderDAL = new OrderDAL();          // Intermediate DAL object
//        OrderDAL savedOrderDAL = new OrderDAL();     // Returned from repository save method
//        OrderDomain mappedDomain = new OrderDomain(); // Final result from mapper
//
//        // Mock the mapper and repository interactions
//        when(orderMapper.convertDomainToOrderDAL(orderDomain)).thenReturn(orderDAL);
//        when(orderRepository.save(orderDAL)).thenReturn(savedOrderDAL);
//        when(orderMapper.convertOrderDALToDomain(savedOrderDAL)).thenReturn(mappedDomain);
//
//        // Act
//        OrderDomain result = orderService.createOrder(orderDomain);
//
//        // Assert
//        assertNotNull(result, "The result should not be null");
//        assertEquals(mappedDomain, result, "The returned domain object should match the mapped domain");
//
//        // Verify interactions
//        verify(orderMapper, times(1)).convertDomainToOrderDAL(orderDomain);
//        verify(orderRepository, times(1)).save(orderDAL);
//        verify(orderMapper, times(1)).convertOrderDALToDomain(savedOrderDAL);
//    }
//
//    @Test
//    void testCreateOrder_RepositorySaveCalledWithCorrectParameters() {
//        // Arrange
//        OrderDomain orderDomain = new OrderDomain(); // Input for the service method
//        OrderDAL orderDAL = new OrderDAL();          // Intermediate DAL object
//        when(orderMapper.convertDomainToOrderDAL(orderDomain)).thenReturn(orderDAL);
//
//        // Act
//        orderService.createOrder(orderDomain);
//
//        // Assert
//        ArgumentCaptor<OrderDAL> captor = ArgumentCaptor.forClass(OrderDAL.class);
//        verify(orderRepository).save(captor.capture());
//        assertEquals(orderDAL, captor.getValue(), "The saved OrderDAL should match the one converted from the domain object");
//    }
//}


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