package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderCouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderListEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public class OrderCouponDao {

  @PersistenceContext private EntityManager entityManager;

  @Transactional
  public OrderCouponEntity getOrderDetails(String couponName) {
    return entityManager
        .createNamedQuery("getCouponDetails", OrderCouponEntity.class)
        .setParameter("couponName", couponName)
        .getSingleResult();
  }

  @Transactional
  public List<OrderListEntity> getAllOrders() {
    List<OrderListEntity> orders =
        entityManager.createNamedQuery("getAllOrders", OrderListEntity.class).getResultList();
    return orders;
  }

  public long saveOrder(OrderListEntity order) {
    entityManager.persist(order);
    return order.getId();
  }

  @Transactional
  public OrderCouponEntity getOrderDetailsById(UUID id) {
    return entityManager
        .createNamedQuery("getCouponDetailsById", OrderCouponEntity.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  @Transactional
  public CustomerAuthEntity getCustomerAuthEntityByAccessToken(String accessToken) {
    try {
      return entityManager
          .createNamedQuery("customerAuthByAccessToken", CustomerAuthEntity.class)
          .setParameter("accessToken", accessToken)
          .getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }

  public List<OrderListEntity> getAllOrderByCustomerId(String customerId) {
    try {
      return entityManager
          .createNamedQuery("getAllOrdersByCustomerId", OrderListEntity.class)
          .setParameter("customerId", customerId)
          .getResultList();
    } catch (NoResultException ex) {
      return null;
    }
  }
}
