package com.b8t.Order_service.business;

import com.b8t.Order_service.model.DAL.OrderDAL;
import com.b8t.Order_service.model.OrderDomain;
import com.b8t.Order_service.persistance.OrderRepository;
import com.b8t.Order_service.utilities.OrderMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository; // Single repository for all operations

    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderDomain createOrder(OrderDomain orderDomain) {
        // First validate input
        if (orderDomain == null) {
            throw new IllegalArgumentException("OrderDomain cannot be null");
        }

        // Convert to DAL
        OrderDAL orderDAL = orderMapper.convertDomainToOrderDAL(orderDomain);
        if (orderDAL == null) {
            throw new IllegalStateException("Failed to map order domain to DAL");
        }

        // Save and convert back to domain
        return orderMapper.convertOrderDALToDomain(orderRepository.save(orderDAL));
    }

}
