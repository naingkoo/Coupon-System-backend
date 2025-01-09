package com.coupon.controller;

import com.coupon.model.CategoryDTO;
import com.coupon.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        return Cservice.saveCategory(dto);

    }

    @GetMapping("/public/list")
    public List<CategoryDTO> shwoAllCategory() {
        return Cservice.showAllCategory();
    }

    @PutMapping("/updateCategory/{id}")
    public CategoryDTO updatebyId(@PathVariable("id")Integer id, @RequestBody CategoryDTO dto) {

        return Cservice.updateCategoryById(id,dto);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<CategoryDTO> softDeleteCategory(@PathVariable Integer id) {
        Cservice.softDeleteCategory(id);
        CategoryDTO categoryDTO=new CategoryDTO();
        return ResponseEntity.ok(categoryDTO);
    }
}
