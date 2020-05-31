package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CategoryDao {
  @PersistenceContext private EntityManager entityManager;

  public Category getCategoryByCategoryUuid(String uuid) {
    try {
      return entityManager
          .createNamedQuery("getCategoryAttributesByUuid", Category.class)
          .setParameter("uuid", uuid)
          .getSingleResult();
    } catch (NoResultException noResultException) {
      return null;
    }
  }
}
