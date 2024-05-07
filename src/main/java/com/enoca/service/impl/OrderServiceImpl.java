package com.enoca.service.impl;

import com.enoca.model.*;
import com.enoca.repository.*;
import com.enoca.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailsRepository orderDetailsRepository;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final CustomerRepository customerRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
    }


    @Override
    public void PlaceOrder(Cart cart) {
        Order order = new Order(); //Yeni order oluşturur ve set ederiz.

        order.setTotalPrice(cart.getTotalPrices());
        order.setCustomer(cart.getCustomer());
        order.setPlaced(true);
        order.setOrderDate(LocalDateTime.now());

        List<OrderDetails> orderDetailsList = new ArrayList<>(); //Yeni OrderDetailsList oluştururuz

        for (CartItem item : cart.getCartItem()){//Cart içerisindeki cartitem ı dönüp ordetDetailsListe set eder
            OrderDetails orderDetails = new OrderDetails();

            orderDetails.setOrder(order);
            orderDetails.setProduct(item.getProduct());
            orderDetails.setQuantity(item.getQuantity());
            orderDetails.setUnitPrice(item.getProduct().getPrice());
            orderDetails.setCreatedAt(LocalDateTime.now());

            orderDetailsRepository.save(orderDetails);
            orderDetailsList.add(orderDetails);

            cartItemRepository.delete(item);
        }

        order.setOrderDetailsList(orderDetailsList);
        cart.setCartItem(new HashSet<>());
        cart.setTotalItems(0);
        cart.setTotalPrices(0);

        cartRepository.save(cart);
        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }
}
