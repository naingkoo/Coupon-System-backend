package com.coupon.service;

import com.coupon.entity.CategoryEntity;
import com.coupon.model.CategoryDTO;
import com.coupon.reposistory.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository Crepo;


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

        List<CategoryEntity> category = Crepo.findAll();
        List<CategoryDTO> dtoList = new ArrayList<>();
        for(CategoryEntity entity: category) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());

            dtoList.add(dto);
        }
        return dtoList;
    }

}
