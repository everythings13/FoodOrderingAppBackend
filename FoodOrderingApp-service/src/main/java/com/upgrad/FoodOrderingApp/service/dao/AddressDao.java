package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param addressEntity
     * @return
     */
    @Transactional
    public AddressEntity save(AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

}
