package com.b8t.Order_service.rabbitMQ;

import com.b8t.Order_service.business.OrderService;
import com.b8t.Order_service.model.DTO.CreateOrderRequest;
import com.b8t.Order_service.model.DTO.CreateOrderResponse;
import com.b8t.Order_service.model.DTO.CreateShippingAddressesRequest;
import com.b8t.Order_service.model.OrderItem;
import com.b8t.Order_service.model.Payment;
import com.b8t.Order_service.utilities.OrderMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class OrdersListener {



    private final OrderMapper orderMapper;

    private final OrderService orderService;

    @Autowired
    public OrdersListener(OrderMapper orderMapper, OrderService orderService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
    }

    @RabbitListener(queues = "order-created.service.queue")
    public void receivePaymentAndConvertToOrder(Payment payment) {
        // Map payment to the create order request
        CreateOrderRequest createOrderRequest = buildCreateOrderRequest(payment);

        // Create the order and map the response
        CreateOrderResponse createOrderResponse = createOrder(createOrderRequest);

        System.out.println(createOrderResponse);
    }

    public CreateOrderRequest buildCreateOrderRequest(Payment payment) {
        // Initialize the CreateOrderRequest and set properties based on the Payment
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setUserId(payment.getShopperId());
        createOrderRequest.setTotalPrice(payment.getTotalPrice());
        createOrderRequest.setStatus("pending");
        createOrderRequest.setShippingAddresses(mapPaymentShippingAddressesToCreateShippingAddressesRequest(payment));
        createOrderRequest.setOrderedItems(mapPaymentProductsToOrderItems(payment));

        return createOrderRequest;
    }

    public CreateOrderResponse createOrder(CreateOrderRequest createOrderRequest) {
        // Convert the CreateOrderRequest to a domain object, create the order, and convert the response
        return orderMapper.convertDomainToCreateOrderResponse(
                orderService.createOrder(orderMapper.convertCreateOrderRequestToDomain(createOrderRequest))
        );
    }


    // Mapping ShippingAddresses from Payment to expected format
    public CreateShippingAddressesRequest mapPaymentShippingAddressesToCreateShippingAddressesRequest(Payment payment) {
        return CreateShippingAddressesRequest.builder()
                .address1(payment.getShippingAddresses().getAddress1())
                .address2(payment.getShippingAddresses().getAddress2())
                .city(payment.getShippingAddresses().getCity())
                .state(payment.getShippingAddresses().getState())
                .country(payment.getShippingAddresses().getCountry())
                .zip(payment.getShippingAddresses().getZip())
                .email(payment.getShippingAddresses().getEmail())
                .build();
    }

    // Mapping Products from Payment to expected format
    public Set<OrderItem> mapPaymentProductsToOrderItems(Payment payment) {
        Set<OrderItem> orderItems = new HashSet<>(); // Set to hold OrderItem objects

        // Check if payment or products are null to avoid NullPointerException
        if (payment == null || payment.getProducts() == null) {
            return orderItems; // Return an empty set if there's nothing to process
        }

        // Loop through each Product in the Payment's products set
        for (OrderItem product : payment.getProducts()) {
            if (product != null) { // Ensure product is not null
                // Create an OrderItem based on the Product's attributes
                OrderItem orderItem = OrderItem.builder() // Assuming OrderItem is a class you're working with
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .sellerId(product.getSellerId())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .build();
                // Add the constructed OrderItem to the orderItems set
                orderItems.add(orderItem);
            }
        }

        return orderItems; // Return the Set<OrderItem>
    }



}
