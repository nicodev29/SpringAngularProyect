package com.company.inventory.service;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponse;
import com.company.inventory.response.ProductResponseRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements IProductService{
    private final ICategoryDao categoryDao;
    private final IProductDao productDao;
    public ProductServiceImp(ICategoryDao categoryDao, IProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @Override
    public ResponseEntity<ProductResponseRest> saveProduct(Product product, Long CategoriId) {

        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
            Optional<Category> category = categoryDao.findById(CategoriId);
            if (category.isPresent()) {
                product.setCategory(category.get());
            } else {
                response.setMetadata("ERROR", "01", "Category not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Product productSave = productDao.save(product);

            list.add(productSave);
            response.getProductResponse().setProducts(list);
            response.setMetadata("OK", "00", "Product saved");

        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("ERROR", "02", "Product not saved");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
