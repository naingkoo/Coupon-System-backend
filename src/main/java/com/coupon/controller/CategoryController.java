package com.coupon.controller;

import com.coupon.model.CategoryDTO;
import com.coupon.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Category")
public class CategoryController {

    @Autowired
    private CategoryService Cservice;


    @PostMapping("/create")
    public CategoryDTO saveCategory(@RequestBody CategoryDTO dto){
        System.out.println("errorrrrrrrrrrrrrrr"+dto.getName());
        return Cservice.saveCategory(dto);

    }

    @GetMapping("/public/list")
    public List<CategoryDTO> shwoAllCategory() {
        return Cservice.showAllCategory();
    }
}
