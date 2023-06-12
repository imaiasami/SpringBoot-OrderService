package com.example.order.model.product;

import com.example.order.model.order.OrderProductForm;
import lombok.Data;

@Data
public class Product {
    private Long product_id;
    private String name;
    private int stock;
    private long price;

    public void ajustStock(Integer count) {
        this.stock = this.stock + count;
    }

    public static OrderProductForm toOrderProductForm(Product product) {
        OrderProductForm orderProductForm = new OrderProductForm();
        orderProductForm.setProduct_id(product.product_id);
        orderProductForm.setName(product.getName());
        orderProductForm.setStock(product.getStock());
        orderProductForm.setPrice(product.getPrice());
        orderProductForm.setCount(1);

        return orderProductForm;
    }
}
