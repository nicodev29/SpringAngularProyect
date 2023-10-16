package com.company.inventory.service;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import org.springframework.http.ResponseEntity;

public interface IProductService {

    ResponseEntity<ProductResponseRest> saveProduct(Product product, Long categoriId);
    ResponseEntity<ProductResponseRest> getProductById(Long Id);


}
