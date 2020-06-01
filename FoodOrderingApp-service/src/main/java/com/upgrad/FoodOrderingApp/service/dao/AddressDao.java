package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    @Transactional
    public AddressEntity getAddressByUUID(String addressId) {
        try {
            return entityManager.createNamedQuery("getAddressByUUID", AddressEntity.class)
                    .setParameter("uuid", addressId)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    @Modifying
    public AddressEntity deleteAddress(AddressEntity addressEntity)
    {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

}
