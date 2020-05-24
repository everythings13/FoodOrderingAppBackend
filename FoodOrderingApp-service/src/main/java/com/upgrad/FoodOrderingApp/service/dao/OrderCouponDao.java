package com.upgrad.FoodOrderingApp.service.dao;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.upgrad.FoodOrderingApp.service.entity.OrderCouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderListEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderCouponDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public OrderCouponEntity getOrderDetails(String couponName){
       return entityManager.createNamedQuery("getCouponDetails",OrderCouponEntity.class).setParameter(couponName,couponName).
               getSingleResult();
    }

    @Transactional
    public List<OrderListEntity> getAllOrders(){
        List<OrderListEntity> orders= entityManager.createNamedQuery("getAllOrders", OrderListEntity.class).getResultList();
        return orders;
    }

    public long saveOrder(OrderListEntity order){
        entityManager.persist(order);
        return order.getId();
    }



}
