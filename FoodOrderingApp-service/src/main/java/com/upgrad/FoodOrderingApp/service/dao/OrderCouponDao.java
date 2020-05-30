package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

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
    try{
    return entityManager
        .createNamedQuery("getCouponDetails", OrderCouponEntity.class)
        .setParameter("couponName", couponName)
        .getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }

  @Transactional
  public List<OrderListEntity> getAllOrders() {
    List<OrderListEntity> orders =
        entityManager.createNamedQuery("getAllOrders", OrderListEntity.class).getResultList();
    return orders;
  }

  @Transactional
  public long saveOrder(OrderListEntity order) {
    entityManager.persist(order);
    return order.getId();
  }

  @Transactional
  public OrderCouponEntity getCouponDetailsByUUId(String uuid) {
    try{
    OrderCouponEntity entity =
        entityManager
            .createNamedQuery("getCouponDetailsByUUId", OrderCouponEntity.class)
            .setParameter("uuid", uuid.toString())
            .getSingleResult();
    return entity;
    }catch (NoResultException ex){
      return null;
    }

  }

  @Transactional
  public OrderCouponEntity getCouponDetailsById(Integer id) {
    try{
      OrderCouponEntity entity =
              entityManager
                      .createNamedQuery("getCouponDetailsById", OrderCouponEntity.class)
                      .setParameter("id", id)
                      .getSingleResult();
      return entity;
    }catch (NoResultException ex){
      return null;
    }

  }
 @Transactional
  public List<CustomerAddressEntity> getCustomerAddress(CustomerEntity customerEntity) {
     try{
       List<CustomerAddressEntity> entity = entityManager.createNamedQuery("addressByCustomer",CustomerAddressEntity.class).setParameter("customerEntity",customerEntity).getResultList();
       return entity;
     }catch (NoResultException ex){
       return null;
     }
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
@Transactional
  public List<OrderListEntity> getAllOrderByCustomerId(Integer customerId) {
    try {
      return entityManager
          .createNamedQuery("getAllOrdersByCustomerId", OrderListEntity.class)
          .setParameter("customerId", customerId)
          .getResultList();
    } catch (NoResultException ex) {
      return null;
    }
  }
@Transactional
  public AddressEntity getAddressByUUId(String uuid){
    try{
      return entityManager.createNamedQuery("getAddressByUUId",AddressEntity.class).setParameter("uuid",uuid).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }

  @Transactional
  public AddressEntity getAddressById(Integer id){
    try{
      return entityManager.createNamedQuery("getAddressById",AddressEntity.class).setParameter("id",id).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }
  @Transactional
  public CustomerEntity getCustomerByUUId(UUID uuid){
    try{
      return entityManager.createNamedQuery("getCustomerByUUId",CustomerEntity.class).setParameter("uuid",uuid).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }
  @Transactional
  public CustomerEntity getCustomerById(Integer id){
    try{
      return entityManager.createNamedQuery("getCustomerById",CustomerEntity.class).setParameter("id",id).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }

  @Transactional
  public OrderListPayment getPaymentByUUId(String uuid){
    try{
      return entityManager.createNamedQuery("getPaymentByUUId",OrderListPayment.class).setParameter("uuid",uuid).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }
  @Transactional
  public OrderListPayment getPaymentById(Integer id){
    try{
      return entityManager.createNamedQuery("getPaymentById",OrderListPayment.class).setParameter("id",id).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }
  @Transactional
  public OrderRestaurantEntity getRestaurantByUUId(String uuid){
    try{
      return entityManager.createNamedQuery("getRestaurantByUUId", OrderRestaurantEntity.class).setParameter("uuid",uuid).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }

  @Transactional
  public OrderRestaurantEntity getRestaurantById(Integer id){
    try{
      return entityManager.createNamedQuery("getRestaurantById", OrderRestaurantEntity.class).setParameter("id",id).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }
}
