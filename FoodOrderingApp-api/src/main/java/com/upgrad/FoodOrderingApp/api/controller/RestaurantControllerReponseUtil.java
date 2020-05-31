package com.upgrad.FoodOrderingApp.api.controller;

import com.google.common.base.Joiner;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantAddress;
import com.upgrad.FoodOrderingApp.service.entity.State;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RestaurantControllerReponseUtil {
  private static final Joiner COMMA_JOINER = Joiner.on(',');

  private RestaurantControllerReponseUtil() {}

  public static RestaurantListResponse getRestaurantListResponse(List<Restaurant> restaurants) {
    List<RestaurantList> restaurantList =
        restaurants.stream()
            .map(RestaurantControllerReponseUtil::getRestaurantListObject)
            .collect(Collectors.toList());
    return new RestaurantListResponse().restaurants(restaurantList);
  }

  private static RestaurantList getRestaurantListObject(Restaurant restaurant) {
    List<String> categoryNames = getCategoryNames(restaurant);
    RestaurantAddress restaurantAddress = restaurant.getRestaurantAddress();
    return new RestaurantList()
        .id(UUID.fromString(restaurant.getUuid()))
        .restaurantName(restaurant.getRestaurantName())
        .photoURL(restaurant.getPhotoURL())
        .customerRating(restaurant.getCustomerRating())
        .averagePrice(restaurant.getAveragePrice())
        .numberCustomersRated(restaurant.getNumberCustomersRated())
        .address(getResponseAddress(restaurantAddress))
        .categories(COMMA_JOINER.join(categoryNames));
  }

  private static RestaurantDetailsResponseAddress getResponseAddress(
      RestaurantAddress restaurantAddress) {
    return new RestaurantDetailsResponseAddress()
        .id(UUID.fromString(restaurantAddress.getUuid()))
        .flatBuildingName(restaurantAddress.getFlatNumber())
        .locality(restaurantAddress.getLocality())
        .city(restaurantAddress.getCity())
        .pincode(restaurantAddress.getPincode())
        .state(getStateResponse(restaurantAddress.getState()));
  }

  private static RestaurantDetailsResponseAddressState getStateResponse(State state) {
    return new RestaurantDetailsResponseAddressState()
        .id(UUID.fromString(state.getUuid()))
        .stateName(state.getStateName());
  }

  private static List<String> getCategoryNames(Restaurant restaurant) {
    //        List<RestaurantCategory> categoriesByRestaurantId =
    //            restaurantCategoryService.getCategoriesByRestaurantId(restaurant.getId());
    //        return categoriesByRestaurantId.stream()
    //            .map(category -> categoryService.getCategoryNameByCategoryUuid(category.getId()))
    //            .collect(Collectors.toList());
    return restaurant.getCategories().stream()
        .map(Category::getCategoryName)
        .sorted()
        .collect(Collectors.toList());
  }
}
