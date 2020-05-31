package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
  private final RestaurantDao restaurantDao;

  @Autowired
  public RestaurantService(RestaurantDao restaurantDao) {
    this.restaurantDao = restaurantDao;
  }

  /** @return list of restaurants */
  public List<Restaurant> getListOfRestaurants() {
    return restaurantDao.getRestaurantList();
  }

  /** @return list of restaurants by name */
  public List<Restaurant> getListOfRestaurantsByName(String name) {
    return restaurantDao.getRestaurantsByName(name);
  }

  /** @return list of restaurants by categoryID */
  public List<RestaurantCategory> getRestaurantByCategoryId(int categoryID) {
    return restaurantDao.getRestaurantByCategoryId(categoryID);
  }

    public Restaurant getRestaurantByRestaurantUuid(String restaurantUuid) {
        return restaurantDao.getRestaurantByRestaurantUuid(restaurantUuid);
    }
}
