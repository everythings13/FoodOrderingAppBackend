package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomerAddressDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<AddressEntity> getAddressByCustomer(CustomerEntity customerEntity) {
        try {
            return entityManager
                    .createNamedQuery("addressByCustomer", AddressEntity.class)
                    .setParameter("customerEntity", customerEntity)
                    .getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
