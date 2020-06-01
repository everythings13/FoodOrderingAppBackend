package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Repository
public class RestaurantCategoryDao {
  @PersistenceContext private EntityManager entityManager;

//  @Transactional
//  public List<RestaurantCategory> getCategoriesByRestaurantId(int restaurantId) {
//    try {
//      return entityManager
//          .createNamedQuery("getCategoriesByRestaurantId", RestaurantCategory.class)
//          .setParameter("restaurant", restaurantId)
//          .getResultList();
//    } catch (NoResultException noResultException) {
//      return Collections.emptyList();
//    }
//  }
}
