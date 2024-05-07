package com.enoca.controller;

import com.enoca.model.Cart;
import com.enoca.model.Customer;
import com.enoca.model.Order;
import com.enoca.service.CustomerService;
import com.enoca.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/orderCode/{orderId}")
    private Order getOrderForCode(@PathVariable Long orderId){
        //Order id'sine göre order döner
        Order order = orderService.getOrderById(orderId);

        return order;
    }

    @GetMapping("/{customerId}")
    private List<Order> getAllOrderForCustomer(@PathVariable Long customerId){
        //CustomerId'ye ait tüm orderları döner
        Customer customer = customerService.findById(customerId);
        List<Order> orders = customer.getOrders();

        return orders;
    }

    @PostMapping("/placeOrder/{customerId}")
    private String placeOrder(@PathVariable Long customerId){

        Customer customer = customerService.findById(customerId);
        Cart cart = customer.getCart();

        orderService.PlaceOrder(cart);

        return "Siparişiniz başarıyla alındı.";
    }
}
