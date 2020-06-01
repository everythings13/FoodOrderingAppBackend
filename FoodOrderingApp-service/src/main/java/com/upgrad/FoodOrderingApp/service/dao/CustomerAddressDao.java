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

    public CustomerEntity getCustomerByAddress(AddressEntity addressEntity)
    {
        try{
            return entityManager
                    .createNamedQuery("customerByAddress",CustomerEntity.class)
                    .setParameter("addressEntity",addressEntity)
                    .getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }
    }

    public CustomerAddressEntity saveCustomerAddress(CustomerAddressEntity customerAddressEntity){
        entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }

    public CustomerAddressEntity getCustomerAddressByAddress(AddressEntity addressEntity){
        try {
            CustomerAddressEntity customerAddressEntity = entityManager.createNamedQuery("getCustomerAddressByAddress",CustomerAddressEntity.class).setParameter("address_entity",addressEntity).getSingleResult();
            return customerAddressEntity;
        }catch (NoResultException nre){
            return null;
        }
    }
}
