package com.company.inventory.service;

import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;
import org.springframework.http.ResponseEntity;

public interface ICategoryService {
    ResponseEntity<CategoryResponseRest> searchAll();
    ResponseEntity<CategoryResponseRest> getCategoryById(Long id);
    ResponseEntity<CategoryResponseRest> saveCategory(Category category);
    ResponseEntity<CategoryResponseRest> updateCategory(Long id, Category category);
    ResponseEntity<CategoryResponseRest> deleteById(Long id);

}
