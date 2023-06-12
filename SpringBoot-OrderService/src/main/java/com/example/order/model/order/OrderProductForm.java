package com.example.order.model.order;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OrderProductForm {
    @NotNull
    private Long product_id;
    private String name;
    private int stock;
    private long price;
    @Min(1) @Max(999)
    private int count;

    public static Order toOrder(OrderProductForm orderProductForm) {
        Order order = new Order();
        order.setProduct_id(orderProductForm.getProduct_id());
        order.setCount(orderProductForm.getCount());
        return order;
    }
}
