package com.company.inventory.service.imp;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements ICategoryService {

    @Autowired
    private final ICategoryDao categoryDao;

    public CategoryServiceImp(ICategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CategoryResponseRest> searchAll() {
        CategoryResponseRest response = new CategoryResponseRest();
        try {
            List<Category> category = (List<Category>) categoryDao.findAll();
            response.getCategoryResponse().setCategory(category);
            response.setMetadata("OK", "00", "Success");
        } catch (Exception e) {
            response.setMetadata("ERROR", "99", "Failed");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CategoryResponseRest> getCategoryById(Long id) {
        CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();

        try {
            Optional<Category> category = categoryDao.findById(id);

            if (category.isPresent()) {
                list.add(category.get());
                response.getCategoryResponse().setCategory(list);
                response.setMetadata("OK", "00", "Category found");

            } else {
                response.setMetadata("ERROR", "01", "Category not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            response.setMetadata("ERROR", "99", "Failed");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CategoryResponseRest> saveCategory(Category category) {

        CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();

        try {
            Category categorySave = categoryDao.save(category);
            list.add(categorySave);
            response.setMetadata("OK", "00", "Category saved!");
            response.getCategoryResponse().setCategory(list);
        } catch (Exception e) {
            response.setMetadata("ERROR", "99", "Failed to save category");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CategoryResponseRest> updateCategory(Long id, Category category) {

        CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();

        try {
            Optional<Category> categorySearch = categoryDao.findById(id);
            if (categorySearch.isPresent()) {
                categorySearch.get().setName(category.getName());
                categorySearch.get().setDescription(category.getDescription());
                categoryDao.save(categorySearch.get());
                list.add(categorySearch.get());
                response.setMetadata("OK", "00", "Category updated!");
                response.getCategoryResponse().setCategory(list);
            } else {
                response.setMetadata("ERROR", "01", "Category not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("ERROR", "99", "Failed to update category");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<CategoryResponseRest> deleteById(Long id) {
        CategoryResponseRest response = new CategoryResponseRest();

        try {
            Optional<Category> categorySearch = categoryDao.findById(id);
            if (categorySearch.isPresent()) {
                categoryDao.deleteById(id);
                response.setMetadata("OK", "00", "Category deleted!");
            } else {
                response.setMetadata("ERROR", "01", "Category not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.setMetadata("ERROR", "99", "Failed to delete category");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CategoryResponseRest> getCategoryByName(String name ) {
        CategoryResponseRest response = new CategoryResponseRest();
        List<Category> list = new ArrayList<>();

        try {
            List<Category> category = categoryDao.findByName(name + "%");
            response.getCategoryResponse().setCategory(category);
            response.setMetadata("OK", "00", "Success");
        } catch (Exception e) {
            response.setMetadata("ERROR", "99", "Failed");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
