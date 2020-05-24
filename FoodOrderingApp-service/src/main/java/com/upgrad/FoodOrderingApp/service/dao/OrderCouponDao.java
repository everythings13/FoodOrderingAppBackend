package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.OrderCouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class OrderCouponDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public OrderCouponEntity getOrderDetails(String couponName){
       return entityManager.createNamedQuery("getCouponDetails",OrderCouponEntity.class).setParameter(couponName,couponName).
               getSingleResult();
    }

}
