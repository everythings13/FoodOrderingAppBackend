package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
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

    public CustomerEntity getCustomerByUUID(final String userUuid) {
        return entityManager.createNamedQuery("customerByUuid", CustomerEntity.class).setParameter("uuid", userUuid).getSingleResult();
    }

    public CustomerEntity getCustomerByEmail(final String email) {
        return entityManager.createNamedQuery("customerByEmail", CustomerEntity.class).setParameter("email", email).getSingleResult();
    }

    public CustomerEntity getCustomerByContactNumber(final String contactnumber) {
        return entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class).setParameter("contactnumber", contactnumber).getSingleResult();
    }

    public CustomerEntity getCustomer(final Integer id)
    {
        return entityManager.createNamedQuery("customerById", CustomerEntity.class).setParameter("id", id).getSingleResult();
    }

    public CustomerAuthEntity getUserByToken(final String token){
        return entityManager.createNamedQuery("customerByAuthtoken", CustomerAuthEntity.class).setParameter("accessToken", token).getSingleResult();
    }

    public CustomerAuthEntity login (CustomerAuthEntity customerAuthEntity)
    {
        entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }

    public CustomerAuthEntity logout (CustomerAuthEntity customerAuthEntity)
    {
        entityManager.merge(customerAuthEntity);
        return customerAuthEntity;
    }

    public CustomerEntity updateCustomer(CustomerEntity customerEntity)
    {
        entityManager.merge(customerEntity);
        return customerEntity;
    }

    /* Update Password*/
    public CustomerEntity updatePassword(CustomerEntity customerEntity){
        entityManager.merge(customerEntity);
        return customerEntity;
    }


}


