package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemsEntity;
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
        return categoryDao.getAllCategories();
    }

    public CategoryEntity getCategoryById(String id){
        CategoryEntity category = categoryDao.getcategoryById(id);
        if(category.getId()==null ||category.getId().equals("")){
            return null;
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
