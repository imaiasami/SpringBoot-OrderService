package com.example.order.model.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class Order {
    private Long order_id;
    private String member_id;
    private long product_id;
    private int count;
    private long order_price;
    private LocalDateTime order_date;

    public void calcOrderPrice(Long price) {
        this.order_price = price * count;
    }
}
