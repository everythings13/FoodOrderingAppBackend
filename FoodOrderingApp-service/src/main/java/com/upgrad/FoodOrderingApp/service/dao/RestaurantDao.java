package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class RestaurantDao {
  @PersistenceContext private EntityManager entityManager;

  public List<Restaurant> getRestaurantList() {
    try {
      return entityManager.createNamedQuery("getAll", Restaurant.class).getResultList();
    } catch (NoResultException noResultException) {
      return Collections.emptyList();
    }
  }

  public List<Restaurant> getRestaurantsByName(String restaurantName) {
    try {
      return entityManager
          .createNamedQuery("findByName", Restaurant.class)
          .setParameter("restaurantName", "%" + restaurantName.toLowerCase() + "%")
          .getResultList();
    } catch (NoResultException noResultException) {
      return Collections.emptyList();
    }
  }

  public List<RestaurantCategory> getRestaurantByCategoryId(int categoryID) {
    try {
      return entityManager
          .createNamedQuery("restaurantsByCategoryId", RestaurantCategory.class)
          .setParameter("id", categoryID)
          .getResultList();
    } catch (NoResultException nre) {
      return Collections.emptyList();
    }
  }

  public Restaurant getRestaurantByRestaurantUuid(String restaurantUuid) {
    try {
      return entityManager
          .createNamedQuery("findRestaurantByUuid", Restaurant.class)
          .setParameter("restaurantUuid", restaurantUuid.toLowerCase())
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  public Restaurant updateRestaurant(Restaurant restaurant) {
    entityManager.merge(restaurant);
    return restaurant;
  }
}
