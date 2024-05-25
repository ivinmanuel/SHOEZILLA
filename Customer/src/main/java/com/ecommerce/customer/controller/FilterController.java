package com.ecommerce.customer.controller;

import com.ecommerce.library.service.CategoryService;
import org.springframework.stereotype.Controller;

@Controller
public class FilterController {
    private CategoryService categoryService;

    public FilterController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

}
