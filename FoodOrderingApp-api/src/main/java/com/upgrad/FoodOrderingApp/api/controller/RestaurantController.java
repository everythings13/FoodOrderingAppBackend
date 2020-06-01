package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantUpdatedResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/restaurant")
public class RestaurantController {

  private static final String EMPTY_STRING_AS_JSON = "\"\"";
  private final RestaurantService restaurantService;
  private final CategoryService categoryService;

  @Autowired
  public RestaurantController(
      RestaurantService restaurantService, CategoryService categoryService) {
    this.restaurantService = restaurantService;
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
      path = "/name/{restaurant_name}")
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

  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      path = "category/{category_id}")
  public ResponseEntity<RestaurantListResponse> getListOfRestaurantsByCategoryId(
      @PathVariable String category_id) throws CategoryNotFoundException {
    if (Strings.isNullOrEmpty(category_id) || category_id.equalsIgnoreCase(EMPTY_STRING_AS_JSON)) {
      throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
    }
    Category category = categoryService.getCategoryNameByCategoryUuid(category_id);
    if (Objects.isNull(category)) {
      throw new CategoryNotFoundException("CNF-002", "No category by this id");
    }
    List<Restaurant> listOfRestaurants =
        restaurantService.getRestaurantByCategoryId(category.getId()).stream()
            .map(RestaurantCategory::getRestaurant)
            .sorted(Comparator.comparing(Restaurant::getRestaurantName))
            .collect(Collectors.toList());

    return new ResponseEntity<>(
        RestaurantControllerReponseUtil.getRestaurantListResponse(listOfRestaurants),
        HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
      path = "/{restaurant_id}")
  public ResponseEntity<RestaurantDetailsResponse> getListOfRestaurantsByRestaurantId(
      @PathVariable String restaurant_id) throws RestaurantNotFoundException {
    if (Strings.isNullOrEmpty(restaurant_id)
        || restaurant_id.equalsIgnoreCase(EMPTY_STRING_AS_JSON)) {
      throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
    }
    Restaurant restaurant = restaurantService.getRestaurantByRestaurantUuid(restaurant_id);
    if (Objects.isNull(restaurant)) {
      throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
    }
    return new ResponseEntity<>(
        RestaurantControllerReponseUtil.getRestaurantDetailsResponse(restaurant), HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      path = "/{restaurant_id}",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<RestaurantUpdatedResponse> updateCustomerRating(
      @RequestHeader("authorization") final String authorization,
      @RequestParam("customer_rating") Double customerRating,
      @PathVariable String restaurant_id)
      throws AuthorizationFailedException, InvalidRatingException, RestaurantNotFoundException {
    String[] bearerToken = authorization.split("Bearer ");
    Restaurant restaurantEntity =
        restaurantService.updateCustomerRating(customerRating, restaurant_id, bearerToken[1]);

    RestaurantUpdatedResponse restaurantUpdatedResponse =
        new RestaurantUpdatedResponse()
            .id(UUID.fromString(restaurantEntity.getUuid()))
            .status("RESTAURANT RATING UPDATED SUCCESSFULLY");

    return new ResponseEntity<>(restaurantUpdatedResponse, HttpStatus.OK);
  }
}
