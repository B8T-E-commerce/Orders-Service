package com.b8t.Order_service.presentation;

import com.b8t.Order_service.business.OrderService;
import com.b8t.Order_service.model.DTO.CreateOrderRequest;
import com.b8t.Order_service.model.DTO.CreateOrderResponse;
import com.b8t.Order_service.utilities.OrderMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        // Convert the CreateOrderRequest to the domain model and create the order
        CreateOrderResponse createOrderResponse = orderMapper.convertDomainToCreateOrderResponse(orderService.createOrder(orderMapper.convertCreateOrderRequestToDomain(createOrderRequest)));

        // Return the response entity with a body
        return new ResponseEntity<>(createOrderResponse, HttpStatus.CREATED);
    }


}
