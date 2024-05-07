package com.enoca.service;

import com.enoca.model.Cart;
import com.enoca.model.CartItem;
import com.enoca.model.Customer;
import com.enoca.model.Product;

import java.util.Optional;
import java.util.Set;

public interface CartService {

    Optional<Cart> getCartByCustomerId(Long id);

    Customer getCustomerById(Long id);

    Cart addProductToCart(Product product, int quantity, Customer customer) throws Exception;

    Cart updateCart(Product product, int quantity, Customer customer);

    Cart removeProductFromCart(Product product,Customer customer);

    void emptyCart(Customer customer);

    CartItem findCartItems(Set<CartItem> cartItems, Long id);

    int totalItems(Set<CartItem> cartItems);

    double totalPrices(Set<CartItem> cartItems);
}
