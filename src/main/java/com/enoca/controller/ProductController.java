package com.enoca.controller;

import com.enoca.model.Product;
import com.enoca.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public List<Product> getAllProduct(){
        return productService.getAllProduct();
    }

    @PostMapping("/add")
    public Product createProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @PostMapping("/update")
    public void updateProduct(@RequestBody Product product){
        productService.updateProduct(product);
    }

    @PostMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }
}
