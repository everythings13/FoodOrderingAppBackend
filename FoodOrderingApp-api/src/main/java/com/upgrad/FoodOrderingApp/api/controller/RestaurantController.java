package com.upgrad.FoodOrderingApp.api.controller;

import com.google.common.base.Joiner;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantCategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantAddress;
import com.upgrad.FoodOrderingApp.service.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

  private static final Joiner COMMA_JOINER = Joiner.on(',');
  private final RestaurantService restaurantService;
  private final RestaurantCategoryService restaurantCategoryService;
  private final CategoryService categoryService;

  @Autowired
  public RestaurantController(
      RestaurantService restaurantService,
      RestaurantCategoryService restaurantCategoryService,
      CategoryService categoryService) {
    this.restaurantService = restaurantService;
    this.restaurantCategoryService = restaurantCategoryService;
    this.categoryService = categoryService;
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<RestaurantListResponse> getListOfRestaurants() {
    List<Restaurant> listOfRestaurants = restaurantService.getListOfRestaurants();
    return new ResponseEntity<>(getRestaurantListResponse(listOfRestaurants), HttpStatus.OK);
  }

  private RestaurantListResponse getRestaurantListResponse(List<Restaurant> restaurants) {
    List<RestaurantList> restaurantList =
        restaurants.stream().map(this::getRestaurantListObject).collect(Collectors.toList());
    return new RestaurantListResponse().restaurants(restaurantList);
  }

  private RestaurantList getRestaurantListObject(Restaurant restaurant) {
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

  private RestaurantDetailsResponseAddress getResponseAddress(RestaurantAddress restaurantAddress) {
    return new RestaurantDetailsResponseAddress()
        .id(UUID.fromString(restaurantAddress.getUuid()))
        .flatBuildingName(restaurantAddress.getFlatNumber())
        .locality(restaurantAddress.getLocality())
        .city(restaurantAddress.getCity())
        .pincode(restaurantAddress.getPincode())
        .state(getStateResponse(restaurantAddress.getState()));
  }

  private RestaurantDetailsResponseAddressState getStateResponse(State state) {
    return new RestaurantDetailsResponseAddressState()
        .id(UUID.fromString(state.getUuid()))
        .stateName(state.getStateName());
  }

  private List<String> getCategoryNames(Restaurant restaurant) {
//        List<RestaurantCategory> categoriesByRestaurantId =
//            restaurantCategoryService.getCategoriesByRestaurantId(restaurant.getId());
//        return categoriesByRestaurantId.stream()
//            .map(category -> categoryService.getCategoryNameByCategoryId(category.getId()))
//            .collect(Collectors.toList());
    return restaurant.getCategories().stream()
        .map(Category::getCategoryName)
        .sorted()
        .collect(Collectors.toList());
  }
}
