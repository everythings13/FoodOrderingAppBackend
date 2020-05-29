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
    public AddressEntity getAddressById(String addressId) {
        try {
            return entityManager.createNamedQuery("getAddressById", AddressEntity.class)
                    .setParameter("uuid", addressId)
                    .getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    @Modifying
    public void deleteAddress(AddressEntity addressEntity)
    {
        entityManager.remove(addressEntity);
    }

}
