package com.b8t.Order_service.persistance;

import com.b8t.Order_service.model.DAL.OrderDAL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderDAL, Long> {

}
