package com.company.inventory.service;

import com.company.inventory.response.CategoryResponseRest;
import org.springframework.http.ResponseEntity;

public interface ICategoryService {
    ResponseEntity<CategoryResponseRest> searchAll();
    ResponseEntity<CategoryResponseRest> getCategoryById(Long id);

}
