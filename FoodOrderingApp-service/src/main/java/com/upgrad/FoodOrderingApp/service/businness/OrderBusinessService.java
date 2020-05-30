package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderCouponDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderCouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderListEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderBusinessService {

  @Autowired private OrderCouponDao orderCouponDao;

  public OrderCouponEntity getCouponDetails(String couponName, String accessToken)
      throws CouponNotFoundException, AuthorizationFailedException {
    validateCustomerAuth(accessToken);
    ValidateCoupon(couponName);
    try {
      OrderCouponEntity orderCoupon = orderCouponDao.getOrderDetails(couponName);

      return orderCoupon;
    } catch (NoResultException ex) {
      return null;
    }
  }

  public OrderCouponEntity getCouponDetailsByUUid(UUID uuid) throws CouponNotFoundException {
    OrderCouponEntity orderCouponDetails = orderCouponDao.getOrderDetailsById(uuid);
    if (orderCouponDetails == null) {
      throw new CouponNotFoundException("CPF-001", "No Coupon By this Id");
    }
    return orderCouponDetails;
  }

  public long saveOrder(OrderListEntity order) {
    return orderCouponDao.saveOrder(order);
  }

  public String getAllOrders(String accessToken) throws AuthorizationFailedException {
    validateCustomerAuth(accessToken);
   CustomerEntity customerDetails= retrieveCustomerId(accessToken);
    List<OrderListEntity> orderDetails = orderCouponDao.getAllOrderByCustomerId(customerDetails.getUuid());
    if(orderDetails==null){
      throw new NoResultException("Order Details Not Found for CustomerId"+customerDetails.getFirstName());
    }
    return null;
  }

  private CustomerEntity retrieveCustomerId(String accessToken) {
    CustomerEntity customerDetails =
            orderCouponDao.getCustomerAuthEntityByAccessToken(accessToken).getCustomerEntity();
    return customerDetails;
  }

  private void validateCustomerAuth(String accessToken) throws AuthorizationFailedException {
    CustomerAuthEntity customerAuthEntity =
        orderCouponDao.getCustomerAuthEntityByAccessToken(accessToken);
    if (customerAuthEntity == null || customerAuthEntity.getAccessToken() == null) {
      throw new AuthorizationFailedException("ATHR_001", "Customer is not Logged in.");
    } else if (customerAuthEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(
          "ATHR_002", "Customer is logged out. Log in again to access this endpoint.   ");
    } else if (customerAuthEntity.getExpiresAt().compareTo(LocalDateTime.now()) < 0) {
      throw new AuthorizationFailedException(
          "ATHR_003", "Your session is expired. Log in again to access this endpoint.");
    }
  }

  private void ValidateCoupon(String couponName) throws CouponNotFoundException {
    if (couponName == null) {
      throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
    }
  }
}
