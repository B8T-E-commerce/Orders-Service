package com.b8t.Order_service.utilities;

import com.b8t.Order_service.model.DAL.OrderDAL;
import com.b8t.Order_service.model.DTO.CreateOrderRequest;
import com.b8t.Order_service.model.DTO.CreateOrderResponse;
import com.b8t.Order_service.model.DTO.CreateShippingAddressesRequest;
import com.b8t.Order_service.model.OrderDomain;
import com.b8t.Order_service.model.OrderItem;
import com.b8t.Order_service.model.ShippingAddresses;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class OrderMapper {

    /// DTO -> Domain

    // CreateOrderRequest -> OrderDomain
    public OrderDomain convertCreateOrderRequestToDomain(CreateOrderRequest createOrderRequest){
        if(createOrderRequest == null) return null;

        return OrderDomain.builder()
                .userId(createOrderRequest.getUserId())
                .totalPrice(createOrderRequest.getTotalPrice())
                .status(createOrderRequest.getStatus())
                .shippingAddresses(convertCreateShippingAddressesRequestToShippingAddresses(createOrderRequest.getShippingAddresses()))
                .orderedItems(createOrderRequest.getOrderedItems())
                .build();
    }

    //  CreateShippingAddressesRequest -> ShippingAddresses
    public ShippingAddresses convertCreateShippingAddressesRequestToShippingAddresses(CreateShippingAddressesRequest createShippingAddressesRequest){
        if(createShippingAddressesRequest == null) return null;

        return ShippingAddresses.builder()
                .address1(createShippingAddressesRequest.getAddress1())
                .address2(createShippingAddressesRequest.getAddress2())
                .city(createShippingAddressesRequest.getCity())
                .state(createShippingAddressesRequest.getState())
                .zip(createShippingAddressesRequest.getZip())
                .country(createShippingAddressesRequest.getCountry())
                .email(createShippingAddressesRequest.getEmail())
                .build();
    }

//    //  Set<CreateOrderItemRequest> -> Set<OrderItem>
//    public Set<OrderItem> convertCreateOrderItemRequestToOrderItem(Set<CreateOrderItemRequest> createOrderItemRequests, CreateOrderRequest createOrderRequest){
//        if(createOrderItemRequests == null) return null;
//
//        // Convert each CreateOrderItemRequest to OrderItem using Builder
//        Set<OrderItem> orderItems = new HashSet<>();
//        for (CreateOrderItemRequest request : createOrderItemRequests) {
//            OrderItem orderItem = OrderItem.builder()
//                    .productId(request.getProductId())
//                    .productName(request.getProductName())
//                    .sellerId(request.getSellerId())
//                    .quantity(request.getQuantity())
//                    .price(request.getPrice())
////                    .order(convertDomainToOrderDAL(convertCreateOrderRequestToDomain(createOrderRequest))) //here i need to add orderDAL object!!!
//                    .build();
//            orderItems.add(orderItem);
//        }
//
//        return orderItems;
//    }




    /// Domain -> DTO

    //  OrderDomain -> CreateOrderResponse
    public CreateOrderResponse convertDomainToCreateOrderResponse(OrderDomain orderDomain){
        if(orderDomain == null) return null;

        return CreateOrderResponse.builder()
                .orderId(orderDomain.getId())
                .build();
    }




    /// Domain -> DAL

    // OrderDomain -> OrderDAL
    public OrderDAL convertDomainToOrderDAL(OrderDomain orderDomain){
        if (orderDomain == null) return null;

        // Create the OrderDAL instance first
        OrderDAL orderDAL = OrderDAL.builder()
                .userId(orderDomain.getUserId())
                .totalPrice(orderDomain.getTotalPrice())
                .status(orderDomain.getStatus())
                .shippingAddresses(orderDomain.getShippingAddresses())
                .build();

        // Get the ordered items from the domain model
        Set<OrderItem> orderedItems = orderDomain.getOrderedItems();

        // Add each OrderItem to OrderDAL using the helper method
        if (orderedItems != null && !orderedItems.isEmpty()) {
            for (OrderItem item : orderedItems) {
                orderDAL.addOrderItem(item); // Helper method sets the order reference in each item
            }
        }

        return orderDAL;
    }




    /// DAL -> Domain

    //  OrderDAL -> OrderDomain
    public OrderDomain convertOrderDALToDomain(OrderDAL orderDAL){
        if(orderDAL == null) return null;

        return OrderDomain.builder()
                .id(orderDAL.getId())
                .userId(orderDAL.getUserId())
                .totalPrice(orderDAL.getTotalPrice())
                .status(orderDAL.getStatus())
                .createdAt(orderDAL.getCreatedAt())
                .updatedAt(orderDAL.getUpdatedAt())
                .shippingAddresses(orderDAL.getShippingAddresses())
                .orderedItems(orderDAL.getOrderedItems())
                .build();
    }
}
