package com.company.inventory.controller;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.service.IProductService;
import com.company.inventory.utils.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;




@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class ProductRestController {

    private final IProductService productService;

    public ProductRestController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseRest> save(

            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("quantity") int quantity,
            @RequestParam("categoryID") Long categoryID

    ) throws IOException {

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setImage(Util.compressZLib(file.getBytes()));

        ResponseEntity<ProductResponseRest> response = productService.saveProduct(product, categoryID);
        return response;
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseRest> getById(@PathVariable("id") Long id) {

        ResponseEntity<ProductResponseRest> response = productService.getProductById(id);
        return response;
    }

}

