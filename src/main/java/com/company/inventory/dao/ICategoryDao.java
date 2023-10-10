package com.company.inventory.dao;

import com.company.inventory.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICategoryDao extends CrudRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.name LIKE :name%")
    List<Category> findByName(@Param("name") String name);

}
