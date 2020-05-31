package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemsEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryBusinessService {
    @Autowired
    private CategoryDao categoryDao;

    public List<CategoryEntity> getAllCategories(){
        List<CategoryEntity> categories= categoryDao.getAllCategories();
        return categories;
    }

    public CategoryEntity getCategoryById(String id) throws CategoryNotFoundException {
        CategoryEntity category = categoryDao.getcategoryById(id);
        if(category==null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        return category;
    }

    public List<ItemsEntity> getItems (BigInteger categoryId){
        List<CategoryItemEntity> items=categoryDao.getCategoryItems(categoryId);
        List<ItemsEntity> itemDetailsList = new ArrayList<>();
        if(items!=null && items.size()>0){
            List<BigInteger> itemIds = new ArrayList<>();
            items.stream().forEach(item ->{
               itemIds.add(item.getItemId());
            });
            itemDetailsList=categoryDao.getItemsByItemIds(itemIds);
        }
            return itemDetailsList;

    }
}