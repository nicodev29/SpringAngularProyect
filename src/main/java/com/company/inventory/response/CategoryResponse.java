package com.company.inventory.response;

import com.company.inventory.model.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {
    List<Category> category;

}
