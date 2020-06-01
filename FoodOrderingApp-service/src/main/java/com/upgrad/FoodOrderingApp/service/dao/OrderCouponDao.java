package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
  public List<OrderEntity> getAllOrders() {
    List<OrderEntity> orders =
        entityManager.createNamedQuery("getAllOrders", OrderEntity.class).getResultList();
    return orders;
  }

  @Transactional
  public OrderEntity saveOrder(OrderEntity order) {

    entityManager.persist(order);

    return order;
  }
  @Transactional
  public OrderItemEntity saveOrderItems(OrderItemEntity orderItem){

    orderItem.setQuantity(1);
     entityManager.persist(orderItem);
     return  orderItem;

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
          .createNamedQuery("customerByAuthtoken", CustomerAuthEntity.class)
          .setParameter("accessToken", accessToken)
          .getSingleResult();
    } catch (NoResultException ex) {
      return null;
    }
  }
@Transactional
  public List<OrderEntity> getAllOrderByCustomerId(Integer customerId) {
    try {
      return entityManager
          .createNamedQuery("getAllOrdersByCustomerId", OrderEntity.class)
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
      return entityManager.createNamedQuery("customerById",CustomerEntity.class).setParameter("id",id).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }

  @Transactional
  public OrderPayment getPaymentByUUId(String uuid){
    try{
      return entityManager.createNamedQuery("getPaymentByUUId", OrderPayment.class).setParameter("uuid",uuid).getSingleResult();
    }catch (NoResultException ex){
      return null;
    }
  }
  @Transactional
  public OrderPayment getPaymentById(Integer id){
    try{
      return entityManager.createNamedQuery("getPaymentById", OrderPayment.class).setParameter("id",id).getSingleResult();
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

  public ItemsEntity getItems(String itemId){
    /*List<String> uuid = new ArrayList<>();
    items.stream().forEach(item -> {
      uuid.add(item.getUuid());
    });*/
    try{
      ItemsEntity itemEntity=entityManager.createNamedQuery("getItemsByIds",ItemsEntity.class).setParameter("uuid",itemId).getSingleResult();
      return itemEntity;
    }catch (NoResultException ex){
      return null;
    }
  }

  public List<OrderItemEntity> getItemsByOrder(Integer orderId){
    List<OrderItemEntity> items = entityManager.createNamedQuery("getItemsByOrderId", OrderItemEntity.class).setParameter("orderId", orderId).getResultList();
    return items;
  }

  public List<ItemsEntity>  getItemsByOrderId(Integer orderId){
    List<OrderItemEntity> items = entityManager.createNamedQuery("getItemsByOrderId", OrderItemEntity.class).setParameter("orderId", orderId).getResultList();
    List<String> uuid= new ArrayList<>();
    items.stream().forEach(item->{
      uuid.add(item.getItemId().getUuid());
    });
    return entityManager.createNamedQuery("getItemsByIds",ItemsEntity.class).setParameter("uuid",uuid).getResultList();
  }
}
