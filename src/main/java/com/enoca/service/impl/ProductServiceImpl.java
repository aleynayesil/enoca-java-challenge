package com.enoca.service.impl;

import com.enoca.model.Product;
import com.enoca.repository.ProductRepository;
import com.enoca.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(Product product) {
        Product newProduct = new Product();//Yeni product oluşturan method

        newProduct.setProductName(product.getProductName());
        newProduct.setPrice(product.getPrice());
        newProduct.setStock(product.getStock());
        newProduct.setInStock(product.getStock() == 0 ? false : true);
        newProduct.setCreatedAt(LocalDateTime.now());
        newProduct.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public Product updateProduct(Product product) {
        Product updatedProduct = getProductById(product.getId()); //Apiden gelen product id ye göre productı günceller

        updatedProduct.setProductName(product.getProductName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setStock(product.getStock());
        updatedProduct.setInStock(product.getStock() == 0 ? false : true);//ürün stockta yoksa false döner
        updatedProduct.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {productRepository.deleteById(id);}

}
