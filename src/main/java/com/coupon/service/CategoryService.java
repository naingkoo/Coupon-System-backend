package com.coupon.service;

import ch.qos.logback.core.model.Model;
import com.coupon.entity.CategoryEntity;
import com.coupon.model.CategoryDTO;
import com.coupon.reposistory.CategoryRepository;
import com.coupon.responObject.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository Crepo;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryDTO saveCategory(CategoryDTO dto){
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        entity = Crepo.save(entity);

        System.out.println(entity.getName()+"ID:  "+entity.getId());
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    public List<CategoryDTO> showAllCategory() {

        List<CategoryEntity> category = Crepo.findAllActiveCategories();
        List<CategoryDTO> dtoList = new ArrayList<>();
        for(CategoryEntity entity: category) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());

            dtoList.add(dto);
        }
        return dtoList;
    }

    public CategoryDTO updateCategoryById(Integer id, CategoryDTO dto) {


        CategoryEntity existingCategory =Crepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // Update the existing category with data from the DTO
        existingCategory.setName(dto.getName());

        // Add more fields as needed

        // Save the updated entity
        CategoryEntity updatedCategory = Crepo.save(existingCategory);

        // Convert the entity back to a DTO and return
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    public CategoryDTO findById(Integer id) {
        // Fetch the CategoryEntity by ID from the repository
        CategoryEntity category = Crepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));

        // Map the CategoryEntity to CategoryDTO
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());

        return categoryDTO;
    }

    public void softDeleteCategory(Integer id) {
        CategoryEntity category = Crepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        //category.setDelete(true);  // Assuming you have a 'deleted' flag
        Crepo.softDeleted(id);
    }

}
