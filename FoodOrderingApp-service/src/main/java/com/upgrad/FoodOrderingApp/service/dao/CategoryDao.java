package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Repository
public class CategoryDao {
  @PersistenceContext private EntityManager entityManager;

//  @Transactional
//  public Category getCategoryByCategoryId(int id) {
//    try {
//      return entityManager
//          .createNamedQuery("getCategoryAttributesById", Category.class)
//          .setParameter("id", id)
//          .getSingleResult();
//    } catch (NoResultException noResultException) {
//      return null;
//    }
//  }
}
