package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemsEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    //To get ItemEntity by its UUID if no result then null is returned.
    public ItemsEntity getItemByUUID(String uuid) {
        try {
            ItemsEntity itemEntity = entityManager.createNamedQuery("getItemByUUID",ItemsEntity.class).setParameter("uuid",uuid).getSingleResult();
            return itemEntity;
        }catch (NoResultException nre){
            return null;
        }
    }
}
