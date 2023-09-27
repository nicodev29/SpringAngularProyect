package com.company.inventory.controller;

import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * Method to get all categories
     * @return
     */

    @GetMapping("/categories")
    public ResponseEntity<CategoryResponseRest> findAll(){
        ResponseEntity<CategoryResponseRest> response = categoryService.searchAll();
        return response;
    }

    /**
     * Method to get category by id
     * @param id
     * @return
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> findById(@PathVariable Long id){
        ResponseEntity<CategoryResponseRest> response = categoryService.getCategoryById(id);
        return response;
    }

}
