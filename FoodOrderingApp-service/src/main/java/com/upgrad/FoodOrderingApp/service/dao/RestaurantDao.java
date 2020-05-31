package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
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
}
