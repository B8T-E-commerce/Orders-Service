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
        // Create and populate OrderDAL entity
        return orderMapper.convertOrderDALToDomain(orderRepository.save(orderMapper.convertDomainToOrderDAL(orderDomain)));
    }

}
