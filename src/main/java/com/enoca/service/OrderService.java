package com.enoca.service;

import com.enoca.model.Cart;
import com.enoca.model.Order;


public interface OrderService {

    void PlaceOrder(Cart cart);

    Order getOrderById(Long id);
}
