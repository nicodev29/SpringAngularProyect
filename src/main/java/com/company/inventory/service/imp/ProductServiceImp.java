package com.company.inventory.service.imp;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IProductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.service.IProductService;
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
    public ResponseEntity<ProductResponseRest> saveProduct(Product product, Long categoryId) {

        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
            Optional<Category> category = categoryDao.findById(categoryId);
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
            } else {
                response.setMetadata("ERROR", "01", "Product not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("ERROR", "02", "Server Error");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);  // Esta línea estaba faltante
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ProductResponseRest> findAll() {
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
                response.setMetadata("OK", "00", "No products found");
                response.getProductResponse().setProducts(new ArrayList<>()); // Lista vacía
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.getStackTrace();
            response.setMetadata("ERROR", "02", "Server Error");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ProductResponseRest> updateProduct(Product product, Long categoryId, Long Id) {
        ProductResponseRest response = new ProductResponseRest();
        List<Product> list = new ArrayList<>();

        try {
            Category category = categoryDao.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            Product existingProduct = productDao.findById(Id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            updateProductDetails(existingProduct, product);
            existingProduct.setCategory(category);

            Product savedProduct = productDao.save(existingProduct);
            list.add(savedProduct);

            response.getProductResponse().setProducts(list);
            response.setMetadata("OK", "00", "Product updated");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            return buildErrorResponse("01", e.getMessage());
        } catch (Exception e) {
            // Consider logging the exception
            return buildErrorResponse("02", "Product not updated");
        }
    }
    private void updateProductDetails(Product existingProduct, Product newProductData) {
        existingProduct.setName(newProductData.getName());
        existingProduct.setPrice(newProductData.getPrice());
        existingProduct.setQuantity(newProductData.getQuantity());
        existingProduct.setImage(newProductData.getImage());
    }
    private ResponseEntity<ProductResponseRest> buildErrorResponse(String errorCode, String errorMessage) {
        ProductResponseRest response = new ProductResponseRest();
        response.setMetadata("ERROR", errorCode, errorMessage);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}

