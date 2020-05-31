package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
public class RestaurantService {
  private static final int ONE = 1;
  private static final int SCALE = 2;
  private static final int FIVE = 5;
  private final RestaurantDao restaurantDao;
  private final CustomerService customerService;

  @Autowired
  public RestaurantService(RestaurantDao restaurantDao, CustomerService customerService) {
    this.restaurantDao = restaurantDao;
    this.customerService = customerService;
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

  /** @return list of restaurants by restaurantUuid */
  public Restaurant getRestaurantByRestaurantUuid(String restaurantUuid) {
    return restaurantDao.getRestaurantByRestaurantUuid(restaurantUuid);
  }

  @Transactional
  public Restaurant updateCustomerRating(
      Double customerRating, String restaurant_id, String authorizationToken)
      throws RestaurantNotFoundException, InvalidRatingException, AuthorizationFailedException {
    customerService.getCustomer(authorizationToken);
    if (Strings.isEmpty(restaurant_id) || restaurant_id.equalsIgnoreCase("\"\"")) {
      throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
    }
    Restaurant restaurantByRestaurantUuid =
        restaurantDao.getRestaurantByRestaurantUuid(restaurant_id);

    if (Objects.isNull(restaurantByRestaurantUuid)) {
      throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
    }
    if (Objects.isNull(customerRating)
        || customerRating.isNaN()
        || customerRating < ONE
        || customerRating > FIVE) {
      throw new InvalidRatingException("IRE-001", "Restaurant should be in the range of 1 to 5");
    }
    Restaurant restaurant = updatedRestaurantEntity(customerRating, restaurantByRestaurantUuid);
    return restaurantDao.updateRestaurant(restaurant);
  }

  private Restaurant updatedRestaurantEntity(Double customerRating, Restaurant restaurant) {
    BigDecimal oldAvgRating =
        restaurant
            .getCustomerRating()
            .multiply(new BigDecimal(restaurant.getNumberCustomersRated()));
    BigDecimal newRating = oldAvgRating.add(BigDecimal.valueOf(customerRating));
    BigDecimal newAvgRating =
        newRating.divide(
            BigDecimal.valueOf(restaurant.getNumberCustomersRated() + ONE),
            SCALE,
            RoundingMode.DOWN);
    restaurant.setCustomerRating(newAvgRating);
    restaurant.setNumberCustomersRated(restaurant.getNumberCustomersRated() + ONE);
    return restaurant;
  }
}
