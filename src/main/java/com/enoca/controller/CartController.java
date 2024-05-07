package com.enoca.controller;

import com.enoca.dto.AddProductToCartRequest;
import com.enoca.dto.UpdateCartRequest;
import com.enoca.model.Cart;
import com.enoca.model.Customer;
import com.enoca.model.Product;
import com.enoca.service.CartService;
import com.enoca.service.CustomerService;
import com.enoca.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private final ProductService productService;

    private final CustomerService customerService;

    public CartController(CartService cartService, ProductService productService, CustomerService customerService) {
        this.cartService = cartService;
        this.productService = productService;
        this.customerService = customerService;
    }

    @GetMapping()
    public Optional<Cart> getAllCart(@RequestBody Customer customer){

        return cartService.getCartByCustomerId(customer.getId());
    }

    @PostMapping("add/{customerId}/{productId}")
    public String addProductToCart(@PathVariable Long customerId,
                                   @PathVariable Long productId,
                                   @RequestBody AddProductToCartRequest request) throws Exception {

        Customer customer = cartService.getCustomerById(customerId);

        Product product = productService.getProductById(productId);

        cartService.addProductToCart(product,request.quantity(),customer);

        return "Ürün Sepetinize Eklendi";
    }

    @PostMapping("/update/{customerId}/{productId}")
    public String updateCart(@PathVariable Long customerId,
                             @PathVariable Long productId,
                             @RequestBody UpdateCartRequest request){

        Customer customer = cartService.getCustomerById(customerId);

        Product product = productService.getProductById(productId);

        cartService.updateCart(product, request.quantity(), customer);

        return "Ürün güncellenmiştir";
    }

    @PostMapping("/empty/{customerId}")
    public String emptyCart(@PathVariable Long customerId){

        Customer customer = cartService.getCustomerById(customerId);

        cartService.emptyCart(customer);

        return "Sepetiniz Silindi";
    }

   @PostMapping("/delete/{customerId}")
    public String removeProductFromCart(@PathVariable Long customerId,
                             @PathVariable Long productId){


        Customer customer = cartService.getCustomerById(customerId);

        Product product = productService.getProductById(productId);

        cartService.removeProductFromCart(product,customer);

        return "Ürün silindi";
    }
}
