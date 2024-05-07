package com.enoca.service;

import com.enoca.model.Cart;
import com.enoca.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProduct();

    Product addProduct(Product product);

    Product getProductById(Long id);
    Product updateProduct(Product product);

   void deleteProduct(Long id);

}
