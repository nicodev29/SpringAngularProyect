package com.company.inventory.controller;

import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200"})
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

    /**
     * Method to save category
     * @param category
     * @return
     */
    @PostMapping("/categories/save")
    public ResponseEntity<CategoryResponseRest> save(@RequestBody Category category){
        ResponseEntity<CategoryResponseRest> response = categoryService.saveCategory(category);
        return response;
    }

    /**
     * Method to update category
     * @param Id and category
     * @return
     */
    @PutMapping("/categories/update/{id}")
    public ResponseEntity<CategoryResponseRest> updateCategory(@PathVariable Long id, @RequestBody Category category){
        ResponseEntity<CategoryResponseRest> response = categoryService.updateCategory(id, category);
        return response;
    }

    /**
     * Method to delete category
     * @param id
     * @return
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseRest> delete(@PathVariable Long id){
        ResponseEntity<CategoryResponseRest> response = categoryService.deleteById(id);
        return response;
    }
    @GetMapping("/categories/name/{name}")
    public ResponseEntity<CategoryResponseRest> findByName(@PathVariable String name){
        ResponseEntity<CategoryResponseRest> response = categoryService.getCategoryByName(name);
        return response;
    }

}
