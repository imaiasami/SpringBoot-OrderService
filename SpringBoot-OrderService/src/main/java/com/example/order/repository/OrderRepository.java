package com.example.order.repository;

import com.example.order.model.order.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderRepository {
    // 상품 주문하기
    void saveOrder(Order order);

    // 전체 주문내역 확인
    List<Order> findOrdersByMemberId(String member_id);

    // 주문 상세내역 확인
    Order findOrderById(Long order_id);

    // 주문 취소하기
    void removeOrderById(Long order_id);
}
