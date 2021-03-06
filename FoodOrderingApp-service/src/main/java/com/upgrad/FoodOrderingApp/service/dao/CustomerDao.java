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

    public CustomerEntity saveCustomer(CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    public CustomerEntity getCustomerByContactNumber(final String contactnumber) {
        try {
            return entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class).setParameter("contactnumber", contactnumber).getSingleResult();
        }catch (Exception e)
        {
            return null;
        }
    }


    public CustomerAuthEntity getUserByToken(final String token){
        try {
            return entityManager.createNamedQuery("customerByAuthtoken", CustomerAuthEntity.class).setParameter("accessToken", token).getSingleResult();
        }catch(Exception e){
            return null;
        }
    }

    public CustomerAuthEntity authenticate (CustomerAuthEntity customerAuthEntity)
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
    public CustomerEntity updateCustomerPassword(CustomerEntity customerEntity){
        entityManager.merge(customerEntity);
        return customerEntity;
    }


}


