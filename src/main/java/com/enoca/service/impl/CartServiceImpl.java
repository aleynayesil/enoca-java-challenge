package com.enoca.service.impl;

import com.enoca.model.Cart;
import com.enoca.model.CartItem;
import com.enoca.model.Customer;
import com.enoca.model.Product;
import com.enoca.repository.CartItemRepository;
import com.enoca.repository.CartRepository;
import com.enoca.repository.CustomerRepository;
import com.enoca.service.CartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CustomerRepository customerRepository;

    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository, CustomerRepository customerRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Optional<Cart> getCartByCustomerId(Long id) {
        Customer customer = getCustomerById(id); //Customer Id'ye göre customerın cartını dönen method
        return cartRepository.findById(customer.getId());
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow();//Id'sine göre customer dönen method
    }

    @Override
    public Cart addProductToCart(Product product, int quantity, Customer customer) {
        Cart cart = customer.getCart(); // Apiden gelen customer a gööre cartı döner

        if(product.isInStock()) {// eğer eklenen ürün stockdaysa ürünü carta ekler

            if (cart == null) {// cart yoksa yeni cart oluşturur
                cart = new Cart();
            }
            Set<CartItem> cartItems = cart.getCartItem(); //carttaki cartitemler için setList oluşturur.
            CartItem cartItem = findCartItems(cartItems, product.getId());//productId'ye göre cartitem döner

            if (cartItems == null) { //Cartiteme ilk defa product ekleniyorsa
                cartItems = new HashSet<>();
                if (cartItem == null) {
                    cartItem = new CartItem(); //Cartiteme apiden gelen product ı set ederiz

                    cartItem.setProduct(product);
                    cartItem.setQuantity(quantity);
                    cartItem.setTotalPrice(quantity * product.getPrice());
                    cartItem.setCart(cart);
                    cartItem.setCreatedAt(LocalDateTime.now());

                    cartItems.add(cartItem);

                    cartItemRepository.save(cartItem);
                }
            } else {
                if (cartItem == null) {
                    cartItem = new CartItem();

                    cartItem.setProduct(product);
                    cartItem.setQuantity(quantity);
                    cartItem.setTotalPrice(quantity * product.getPrice());
                    cartItem.setCart(cart);
                    cartItem.setCreatedAt(LocalDateTime.now());

                    cartItems.add(cartItem);

                    cartItemRepository.save(cartItem);
                } else {
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    cartItem.setTotalPrice(cartItem.getTotalPrice() + quantity * product.getPrice());

                    cartItemRepository.save(cartItem);
                }
            }
            //cartitemler carta set edilir
            cart.setCartItem(cartItems);

            int totalItems = totalItems(cart.getCartItem());
            double totalPrices = totalPrices(cart.getCartItem());

            cart.setTotalItems(totalItems);
            cart.setTotalPrices(totalPrices);
            cart.setCustomer(customer);
            cart.setCreatedAt(LocalDateTime.now());

            customer.setCart(cart);
            customer.setUpdatedAt(LocalDateTime.now());
            // customerRepository.save(customer);

            product.setStock(product.getStock() - quantity);
            product.setInStock(product.getStock() == 0 ? false : true);
            product.setCart(cart);
            product.setUpdatedAt(LocalDateTime.now());

            return cartRepository.save(cart);
        }else {
            throw new RuntimeException("Ürün stokları tükenmiştir"); //isInstock değilse
        }
    }

    @Override
    public Cart updateCart(Product product, int quantity, Customer customer) {
        Cart cart = customer.getCart();  // Apiden gelen customer a gööre cartı döner

        Set<CartItem> cartItems = cart.getCartItem();

        CartItem cartItem = findCartItems(cartItems, product.getId());


        cartItem.setQuantity(quantity); // Apiden girilen yeni değeri set eder
        cartItem.setTotalPrice(quantity * product.getPrice());

        cartItemRepository.save(cartItem);

        int totalItems = totalItems(cart.getCartItem());
        double totalPrices = totalPrices(cart.getCartItem());

        product.setUpdatedAt(LocalDateTime.now());

        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrices);

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeProductFromCart(Product product, Customer customer) {
        Cart cart = customer.getCart();

        Set<CartItem> cartItems = cart.getCartItem();

        CartItem cartItem = findCartItems(cartItems, product.getId());

        cartItems.remove(cartItem);
        //Cart içindeki apiden girilen id'ye göre cartitem 'ı siler
        cartItemRepository.delete(cartItem);

        int totalItems = totalItems(cart.getCartItem());
        double totalPrices = totalPrices(cart.getCartItem());

        cart.setCartItem(cartItems);
        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrices);

        return cartRepository.save(cart);
    }

    @Override
    public void emptyCart(Customer customer) {
        Cart cart = customer.getCart(); //Apiden gelen customer id 'nin cartını siler

        Set<CartItem> cartItems = cart.getCartItem();

        cartItems.removeAll(cartItems);

        cartItemRepository.deleteAll(cartItems);

        int totalItems = totalItems(cart.getCartItem());
        double totalPrices = totalPrices(cart.getCartItem());

        cart.setCartItem(cartItems);
        cart.setTotalItems(totalItems);
        cart.setTotalPrices(totalPrices);

        cartRepository.delete(cart);
    }

    @Override
    public CartItem findCartItems(Set<CartItem> cartItems, Long id) {

        if (cartItems == null){
            return null;
        }

        CartItem cartItem = null;

        for (CartItem item : cartItems){ //Cartitemın product idsi ile apiden gelen id yi karşılaştırı ve bulduğu cartitem ı döner
            if (item.getProduct().getId() == id){
                cartItem = item;
            }
        }
        return cartItem;
    }

    @Override
    public int totalItems(Set<CartItem> cartItems) {
        int totalItems = 0;

        for (CartItem item: cartItems){
            totalItems += item.getQuantity();
        }

        return totalItems;
    }

    @Override
    public double totalPrices(Set<CartItem> cartItems) {
        double totalPrice = 0;

        for (CartItem item : cartItems){
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }


}
