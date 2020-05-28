package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryBusinessService {
    @Autowired
    private CategoryDao categoryDao;

    public List<CategoryEntity> getAllCategories(){
        return categoryDao.getAllCategories();
    }

    public CategoryEntity getCategoryById(UUID id){
        return  categoryDao.getcategoryById(id);
    }
}
