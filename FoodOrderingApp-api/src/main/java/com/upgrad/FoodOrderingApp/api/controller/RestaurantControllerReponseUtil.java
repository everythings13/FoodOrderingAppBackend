package com.upgrad.FoodOrderingApp.api.controller;

import com.google.common.base.Joiner;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.entity.*;

import java.util.Comparator;
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
    return new RestaurantList()
        .id(UUID.fromString(restaurant.getUuid()))
        .restaurantName(restaurant.getRestaurantName())
        .photoURL(restaurant.getPhotoURL())
        .customerRating(restaurant.getCustomerRating())
        .averagePrice(restaurant.getAveragePrice())
        .numberCustomersRated(restaurant.getNumberCustomersRated())
        .address(getResponseAddress(restaurant.getRestaurantAddress()))
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
    return restaurant.getCategories().stream()
        .map(Category::getCategoryName)
        .sorted()
        .collect(Collectors.toList());
  }

  public static RestaurantDetailsResponse getRestaurantDetailsResponse(Restaurant restaurant) {
    return new RestaurantDetailsResponse()
        .id(UUID.fromString(restaurant.getUuid()))
        .restaurantName(restaurant.getRestaurantName())
        .photoURL(restaurant.getPhotoURL())
        .customerRating(restaurant.getCustomerRating())
        .averagePrice(restaurant.getAveragePrice())
        .numberCustomersRated(restaurant.getNumberCustomersRated())
        .address(getResponseAddress(restaurant.getRestaurantAddress()))
        .categories(getCategoryList(restaurant));
  }

  private static List<CategoryList> getCategoryList(Restaurant restaurant) {
    return restaurant.getCategories().stream()
        .map(RestaurantControllerReponseUtil::getCategoryListObject)
        .sorted(Comparator.comparing(CategoryList::getCategoryName))
        .collect(Collectors.toList());
  }

  private static CategoryList getCategoryListObject(Category category) {
    List<ItemList> itemListObjectList =
        category.getItemEntities().stream()
            .map(RestaurantControllerReponseUtil::getItemListObject)
            .collect(Collectors.toList());
    return new CategoryList()
        .id(UUID.fromString(category.getUuid()))
        .categoryName(category.getCategoryName())
        .itemList(itemListObjectList);
  }

  private static ItemList getItemListObject(Item item) {
    return new ItemList()
        .id(UUID.fromString(item.getUuid()))
        .itemName(item.getItemName())
        .price(item.getPrice())
        .itemType(ItemList.ItemTypeEnum.fromValue(item.getType()));
  }

  public static PaymentResponse getPaymentListObject(Payment payment) {
    return new PaymentResponse()
        .id(UUID.fromString(payment.getUuid()))
        .paymentName(payment.getPaymentName());
  }
}
