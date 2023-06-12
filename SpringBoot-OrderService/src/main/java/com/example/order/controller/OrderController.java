package com.example.order.controller;

import com.example.order.model.order.OrderProductForm;
import com.example.order.model.product.Product;
import com.example.order.model.order.MyOrderDTO;
import com.example.order.model.order.Order;
import com.example.order.model.member.Member;
import com.example.order.repository.ProductRepository;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("order")
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @PostMapping("orderProduct")
    public String order(@SessionAttribute("loginMember") Member loginMember,
                        @Validated @ModelAttribute OrderProductForm orderProductForm,
                        BindingResult bindingResult) {
        log.info("order: {}", orderProductForm);
        Product product = productRepository.findProductById(orderProductForm.getProduct_id());

        // 상품 정보가 없으면 주문 처리를 하지 않는다.
        if (product == null) {
            return "redirect:/product/products";
        }
        orderProductForm.setProduct_id(product.getProduct_id());
        orderProductForm.setName(product.getName());
        orderProductForm.setPrice(product.getPrice());
        orderProductForm.setStock(product.getStock());

        if (bindingResult.hasErrors()) {
            return "/product/productInfo";
        }

        // 재고량 보다 주문수량이 더 많으면 주문처리를 하지 않는다.
        if (product.getStock() < orderProductForm.getCount()) {
            bindingResult.rejectValue("count", "countError", "재고 수량이 부족합니다.");
            return "/product/productInfo";
        }

        // 주문 객체를 생성
        Order order = OrderProductForm.toOrder(orderProductForm);
        // 주문자 아이디 저장
        order.setMember_id(loginMember.getMember_id());
        // 전체 주문 가격 계산
        order.calcOrderPrice(product.getPrice());
        // 주문내역 저장
        orderRepository.saveOrder(order);
        // 상품 재고 수정
        product.ajustStock(-order.getCount());
        productRepository.updateProduct(product);

        return "redirect:/product/products";
    }

    @GetMapping("myOrders")
    public String myOrders(@SessionAttribute("loginMember") Member loginMember,
                           Model model) {
        List<Order> orders = orderRepository.findOrdersByMemberId(loginMember.getMember_id());
        log.info("orders: {}", orders);
        List<MyOrderDTO> myOrderDTOs = new ArrayList<>();
        for (Order order : orders) {
            MyOrderDTO myOrderDTO = new MyOrderDTO();
            myOrderDTO.setOrder(order);
            Product product = productRepository.findProductById(order.getProduct_id());
            myOrderDTO.setProduct(product);
            myOrderDTOs.add(myOrderDTO);
        }
        log.info("myOrderDTOs: {}", myOrderDTOs);
        model.addAttribute("myOrderDTOs", myOrderDTOs);
        return "order/myOrders";
    }

    @GetMapping("orderInfo")
    public String orderInfo(@SessionAttribute("loginMember") Member loginMember,
                            @RequestParam Long order_id,
                            Model model) {

        Order order = orderRepository.findOrderById(order_id);
        Product product = productRepository.findProductById(order.getProduct_id());
        MyOrderDTO myOrderDTO = new MyOrderDTO();
        myOrderDTO.setOrder(order);
        myOrderDTO.setProduct(product);
        log.info("myOrderDTO: {}", myOrderDTO);
        model.addAttribute("myOrderDTO", myOrderDTO);
        return "order/orderInfo";
    }

    @GetMapping("withdrawOrder")
    public String withdrawOrder(@SessionAttribute("loginMember") Member loginMember,
                                @RequestParam Long order_id) {
        Order order = orderRepository.findOrderById(order_id);
        if (order.getMember_id().equals(loginMember.getMember_id())) {
            Product findProduct = productRepository.findProductById(order.getProduct_id());
            // 재고 수량 조정
            findProduct.ajustStock(order.getCount());
            // 주문 내역 삭제
            orderRepository.removeOrderById(order_id);
            // 상품 재고 정보 수정
            productRepository.updateProduct(findProduct);
        }

        return "redirect:/order/myOrders";
    }
}
