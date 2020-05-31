package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
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

  /**  @return list of restaurants*/
  public List<Restaurant> getListOfRestaurants() {
    return restaurantDao.getRestaurantList();
  }

  /**  @return list of restaurants*/
  public List<Restaurant> getListOfRestaurantsByName(String name) {
    return restaurantDao.getRestaurantsByName(name);
  }

}
