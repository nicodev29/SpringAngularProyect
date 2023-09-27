package com.company.inventory.service;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;
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
            response.setMetadata("OK", "00", "Category saved!");
            list.add(categorySave);
        } catch (Exception e) {
            response.setMetadata("ERROR", "99", "Failed to save category");
            e.getStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}