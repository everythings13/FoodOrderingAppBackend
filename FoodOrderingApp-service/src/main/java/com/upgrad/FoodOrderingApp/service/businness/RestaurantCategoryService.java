package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantCategoryService
{
    private final RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    public RestaurantCategoryService(RestaurantCategoryDao restaurantCategoryDao) {
        this.restaurantCategoryDao = restaurantCategoryDao;
    }


//    /**  @return list of category Ids per restaurant id*/
//    public List<RestaurantCategory> getCategoriesByRestaurantId(int id) {
//        return restaurantCategoryDao.getCategoriesByRestaurantId(id);
//    }
}
