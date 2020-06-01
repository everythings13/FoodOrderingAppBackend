package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemsEntity;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Repository
public class CategoryDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<CategoryEntity> getAllCategories(){
        try{
        return entityManager.createNamedQuery("getAllCategories",CategoryEntity.class).getResultList();
        }catch (NoResultException ex){
            return null;
        }
    }

    public CategoryEntity getcategoryByUUID(String uuid){
        try{
            CategoryEntity entity=
                    entityManager.createNamedQuery("getCategoryByUUID",CategoryEntity.class).setParameter("uuid",uuid).getSingleResult();
            return entity;
        }catch (NoResultException ex){
            return null;
        }

    }

    public List<CategoryItemEntity> getCategoryItems(Integer categoryId){
        return entityManager.createNamedQuery("getItems",CategoryItemEntity.class).setParameter("categoryId",categoryId).getResultList();
    }

    public List<ItemsEntity> getItemsByItemIds(List<Integer> id){
        return entityManager.createNamedQuery("getItemsByIds",ItemsEntity.class).setParameter("id",id).getResultList();
    }

  /*public CategoryEntity getCategoryByCategoryUuid(String uuid) {
    try {
      return entityManager
          .createNamedQuery("getCategoryAttributesByUuid", Category.class)
          .setParameter("uuid", uuid)
          .getSingleResult();
    } catch (NoResultException noResultException) {
      return null;
    }
  }*/
}
