package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    public CategoryEntity getcategoryById(UUID id){
        return entityManager.createNamedQuery("getCategoryById",CategoryEntity.class).setParameter("id",id).getSingleResult();
    }
}
