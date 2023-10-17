package com.company.inventory.service;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.utils.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements IProductService {
    private final ICategoryDao categoryDao;
    private final IProductDao productDao;

    public ProductServiceImp(ICategoryDao categoryDao, IProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> saveProduct(Product product, Long categoriId) {

        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
            Optional<Category> category = categoryDao.findById(categoriId);
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

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> getProductById(Long Id) {

        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {

            Optional<Product> product = productDao.findById(Id);

            if (product.isPresent()) {

                byte[] imageDescompressed = Util.decompressZLib(product.get().getImage());

                product.get().setImage(imageDescompressed);
                list.add(product.get());
                response.getProductResponse().setProducts(list);
                response.setMetadata("OK", "00", "Product found");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setMetadata("ERROR", "01", "Product not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("ERROR", "02", "Server Error");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> getProductByName(String name) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();
        List<Product> listAux;
        try {

            listAux = productDao.findByNameContainingIgnoreCase(name);

            if (!listAux.isEmpty()) {

                listAux.stream().forEach(product -> {
                    byte[] imageDescompressed = Util.decompressZLib(product.getImage());
                    product.setImage(imageDescompressed);
                    list.add(product);
                });

                response.getProductResponse().setProducts(list);
                response.setMetadata("OK", "00", "Success");


            } else {
                response.setMetadata("ERROR", "01", "Product not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("ERROR", "02", "Server Error");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> deleteProductById(Long Id) {

        ProductResponseRest response = new ProductResponseRest();

        try {
            Optional<Product> product = productDao.findById(Id);

            if (product.isPresent()) {
                productDao.deleteById(Id);
                response.setMetadata("OK", "00", "Product Deleted");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setMetadata("ERROR", "01", "Product not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("ERROR", "02", "Server Error");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductResponseRest> getAll() {
        ProductResponseRest response = new ProductResponseRest();

        List<Product> list = new ArrayList<>();
        List<Product> listAux;
        try {

            listAux = (List<Product>) productDao.findAll();

            if (!listAux.isEmpty()) {

                listAux.stream().forEach(product -> {
                    byte[] imageDescompressed = Util.decompressZLib(product.getImage());
                    product.setImage(imageDescompressed);
                    list.add(product);
                });

                response.getProductResponse().setProducts(list);
                response.setMetadata("OK", "00", "All products");


            } else {
                response.setMetadata("ERROR", "01", "Products not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("ERROR", "02", "Server Error");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

