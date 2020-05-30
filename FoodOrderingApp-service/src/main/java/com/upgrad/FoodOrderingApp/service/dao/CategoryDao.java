package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemsEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
        return entityManager.createNamedQuery("getAllCategories",CategoryEntity.class).getResultList();
    }

    public CategoryEntity getcategoryById(String uuid){
        System.out.println("uuid ----------"+uuid);
        CategoryEntity entity=
         entityManager.createNamedQuery("getCategoryByUUID",CategoryEntity.class).setParameter("uuid",uuid).getSingleResult();
        return entity;
    }

    public List<CategoryItemEntity> getCategoryItems(BigInteger categoryId){
        return entityManager.createNamedQuery("getItems",CategoryItemEntity.class).setParameter("categoryId",categoryId).getResultList();
    }

    public List<ItemsEntity> getItemsByItemIds(List<BigInteger> id){
        return entityManager.createNamedQuery("getItemsByIds",ItemsEntity.class).setParameter("id",id).getResultList();
    }
}
