package com.example.order.controller;

import com.example.order.model.order.OrderProductForm;
import com.example.order.model.product.Product;
import com.example.order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("product")
@Controller
public class ProductController {

    private final ProductRepository productRepository;

    // 아이템 목록
    @GetMapping("products")
    public String list(Model model) {
        model.addAttribute("products", productRepository.findProducts());
        return "/product/productList";
    }

    // 아이템 상세 페이지
    @GetMapping("productInfo")
    public String itemInfo(@RequestParam Long product_id,
                           Model model) {
        Product product = productRepository.findProductById(product_id);
        OrderProductForm orderProductForm = Product.toOrderProductForm(product);
        model.addAttribute("orderProductForm", orderProductForm);
        return "/product/productInfo";
    }
}
