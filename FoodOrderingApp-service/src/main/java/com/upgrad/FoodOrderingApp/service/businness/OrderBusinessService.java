package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderCouponDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderBusinessService {

  @Autowired private OrderCouponDao orderCouponDao;

  public OrderCouponEntity getCouponDetails(String couponName, String accessToken)
      throws CouponNotFoundException, AuthorizationFailedException {
    //validateCustomerAuth(accessToken);
    ValidateCoupon(couponName);
    try {
      OrderCouponEntity orderCoupon = orderCouponDao.getOrderDetails(couponName);

      return orderCoupon;
    } catch (NoResultException ex) {
      return null;
    }
  }

  public OrderCouponEntity getCouponDetailsByUUid(String uuid) throws CouponNotFoundException {
    OrderCouponEntity orderCouponDetails = orderCouponDao.getCouponDetailsByUUId(uuid);
    if (orderCouponDetails == null) {
      throw new CouponNotFoundException("CPF-001", "No Coupon By this Id");
    }
    return orderCouponDetails;
  }



  public OrderCouponEntity getCouponDetailsById(Integer id)  {
    OrderCouponEntity orderCouponDetails = orderCouponDao.getCouponDetailsById(id);
    return orderCouponDetails;
  }

  public OrderListPayment getPaymentDetailsById(Integer id){
    OrderListPayment paymentDetails = orderCouponDao.getPaymentById(id);
    return paymentDetails;
  }
  public CustomerEntity getCustomerById(Integer id){
    CustomerEntity customerDeatils = orderCouponDao.getCustomerById(id);
    return customerDeatils;
  }
  public AddressEntity getAddressDetailsById(Integer id){
    AddressEntity addressDetails = orderCouponDao.getAddressById(id);
    return addressDetails;
  }

  public long saveOrder(OrderListEntity order, String accessToken)
      throws AddressNotFoundException, AuthorizationFailedException, CouponNotFoundException,
          PaymentMethodNotFoundException, RestaurantNotFoundException {
    // validateCustomerAuth(accessToken);

    OrderCouponEntity couponDetails = validateCoupon(order);
    OrderListPayment paymentDetails = validatePayment(order);
    OrderRestaurantEntity restaurantDetails = validateRestaurant(order);
    AddressEntity address = getAddress(order.getAddress().toString());
    // validateAddress(order,address, accessToken);
    // order.setCustomer(retrieveCustomerId(accessToken));

    order.setCustomerId(1);
    order.setRestaurantId(restaurantDetails.getId().intValue());
    order.setAddressId(address.getId());
    order.setPaymentId(paymentDetails.getId().intValue());
    order.setCouponId(couponDetails.getId().intValue());
    return orderCouponDao.saveOrder(order);
  }

  private void validateAddress(OrderListEntity order, AddressEntity address, String accessToken)
      throws AddressNotFoundException, AuthorizationFailedException {

    List<CustomerAddressEntity> addresses =
        orderCouponDao.getCustomerAddress(retrieveCustomerId(accessToken));
    if (!(addresses.contains(address))) {
      throw new AuthorizationFailedException(
          "ATHR-004", "You are not authorized to view/update/delete any one else's address");
    }
  }

  private OrderRestaurantEntity validateRestaurant(OrderListEntity order)
      throws RestaurantNotFoundException {
    OrderRestaurantEntity restaurant =
        orderCouponDao.getRestaurantByUUId(order.getRestaurant().toString());
    if (restaurant == null) {
      throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
    }
    return restaurant;
  }

  private OrderListPayment validatePayment(OrderListEntity order)
      throws PaymentMethodNotFoundException {
    OrderListPayment payment = orderCouponDao.getPaymentByUUId(order.getPayment().toString());
    if (payment == null) {
      throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
    }
    return payment;
  }

  private OrderCouponEntity validateCoupon(OrderListEntity order) throws CouponNotFoundException {
    OrderCouponEntity coupon = orderCouponDao.getCouponDetailsByUUId(order.getCoupon().toString());
    if (coupon == null) {
      throw new CouponNotFoundException("CPF-002", "No coupon by this id");
    }
    return coupon;
  }

  public List<OrderListEntity> getAllOrders(String accessToken)
      throws AuthorizationFailedException {
    // validateCustomerAuth(accessToken);
    // CustomerEntity customerDetails = retrieveCustomerId(accessToken);
    /*List<OrderListEntity> orderDetails =
    orderCouponDao.getAllOrderByCustomerId(customerDetails.getUuid());*/
    List<OrderListEntity> orderDetails = orderCouponDao.getAllOrderByCustomerId(1);
    if (orderDetails == null) {
      throw new NoResultException("Order Details Not Found for CustomerId");
    }
    return orderDetails;
  }

  public AddressEntity getAddress(String uuid) throws AddressNotFoundException {
    AddressEntity address = orderCouponDao.getAddressByUUId(uuid);
    if (address == null) {
      throw new AddressNotFoundException("ANF-003", "No address by this id");
    }

    return address;
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
