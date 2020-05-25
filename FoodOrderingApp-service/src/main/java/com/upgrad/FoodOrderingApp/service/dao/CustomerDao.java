package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {
    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity createCustomer(CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;
    }

   /* public CustomerEntity getCustomerByUUID(final String userUuid) {
        return entityManager.createNamedQuery("customerByUuid", CustomerEntity.class).setParameter("uuid", userUuid).getSingleResult();
    }

    public CustomerEntity getCustomerByUsername(final String username) {
        return entityManager.createNamedQuery("customerByUsername", CustomerEntity.class).setParameter("username", username).getSingleResult();
    }

    public CustomerEntity getCustomer(final Integer id)
    {
        return entityManager.createNamedQuery("customerById", CustomerEntity.class).setParameter("id", id).getSingleResult();
    }

    public UserAuthEntity getUserByToken(final String token){
        return entityManager.createNamedQuery()
    }*/
}


