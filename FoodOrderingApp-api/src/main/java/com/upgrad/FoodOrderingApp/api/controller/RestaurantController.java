package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantCategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

  private static final String EMPTY_STRING_AS_JSON = "\"\"";
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
    return new ResponseEntity<>(
        RestaurantControllerReponseUtil.getRestaurantListResponse(listOfRestaurants),
        HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      path = "/restaurant/name/{restaurant_name}")
  public ResponseEntity<RestaurantListResponse> getListOfRestaurantsByName(
      @PathVariable String restaurant_name) throws RestaurantNotFoundException {
    if (Strings.isNullOrEmpty(restaurant_name)
        || restaurant_name.equalsIgnoreCase(EMPTY_STRING_AS_JSON)) {
      throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
    }
    List<Restaurant> listOfRestaurants =
        restaurantService.getListOfRestaurantsByName(restaurant_name);
    return new ResponseEntity<>(
        RestaurantControllerReponseUtil.getRestaurantListResponse(listOfRestaurants),
        HttpStatus.OK);
  }
}
