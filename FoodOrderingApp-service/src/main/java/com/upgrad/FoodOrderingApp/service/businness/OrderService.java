package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.ZonedDateTime;
import java.util.List;

import static com.upgrad.FoodOrderingApp.service.util.MessageKeys.*;

@Service
@Component
public class OrderService {

  @Autowired private OrderDao orderDao;
  @Autowired private CustomerService customerService;
  @Autowired private PaymentService paymentService;
  @Autowired private RestaurantService restaurantService;
  @Autowired private CustomerDao customerDao;

  public CouponEntity getCouponByCouponName(String couponName)
      throws CouponNotFoundException {
   CouponEntity orderCoupon = orderDao.getOrderDetails(couponName);
    if (orderCoupon == null) {
      throw new CouponNotFoundException("CPF-001", "No coupon by this name");
    }
    return orderCoupon;
  }

  public CouponEntity getCouponDetailsByUUid(String uuid) throws CouponNotFoundException {
    CouponEntity orderCouponDetails = orderDao.getCouponDetailsByUUId(uuid);
    if (orderCouponDetails == null) {
      throw new CouponNotFoundException("CPF-001", "No Coupon By this Id");
    }
    return orderCouponDetails;
  }

  public CouponEntity getCouponDetailsById(Integer id) {
    CouponEntity orderCouponDetails = orderDao.getCouponDetailsById(id);
    return orderCouponDetails;
  }

  public PaymentEntity getPaymentDetailsById(Integer id) throws PaymentMethodNotFoundException {
    PaymentEntity paymentDetails = paymentService.getPaymentById(id);
    return paymentDetails;
  }

  public CustomerEntity getCustomerById(Integer id) {
    CustomerEntity customerDeatils = orderDao.getCustomerById(id);
    return customerDeatils;
  }

  public AddressEntity getAddressDetailsById(Integer id) {
    AddressEntity addressDetails = orderDao.getAddressById(id);
    return addressDetails;
  }

  public OrderEntity saveOrder(OrderEntity order)
      throws AddressNotFoundException, AuthorizationFailedException, CouponNotFoundException,
          PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
    OrderEntity orderDetails = orderDao.saveOrder(order);
    return orderDetails;
  }

  public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {

    OrderItemEntity orderItemDetails = orderDao.saveOrderItems(orderItemEntity);

    return orderItemDetails;
  }

  public ItemsEntity validateItems(String itemId) throws ItemNotFoundException {
    ItemsEntity itemDetails = orderDao.getItems(itemId);
    if (itemDetails == null) {
      throw new ItemNotFoundException("INF-003", "No item by this id exist");
    }
    return itemDetails;
  }

  private void validateAddress(OrderEntity order, AddressEntity address, String accessToken)
      throws AddressNotFoundException, AuthorizationFailedException {

    List<CustomerAddressEntity> addresses =
        orderDao.getCustomerAddress(retrieveCustomerId(accessToken));
    if (!(addresses.contains(address))) {
      throw new AuthorizationFailedException(
          "ATHR-004", "You are not authorized to view/update/delete any one else's address");
    }
  }

  private Restaurant validateRestaurant(OrderEntity order) throws RestaurantNotFoundException {

    return restaurantService.getRestaurantByRestaurantUuid(order.getRestaurant().getUuid());
  }

  private PaymentEntity validatePayment(OrderEntity order) throws PaymentMethodNotFoundException {
    return paymentService.getPaymentByUUID(order.getPayment().getUuid());
  }

  private CouponEntity validateCoupon(OrderEntity order) throws CouponNotFoundException {
    CouponEntity coupon = orderDao.getCouponDetailsByUUId(order.getCoupon().getUuid());
    return coupon;
  }

  public List<OrderEntity> getOrdersByCustomers(String accessToken)
      throws AuthorizationFailedException {
    CustomerEntity customerDetails = validateCustomerAuth(accessToken);
    return getOrdersByCustomers(customerDetails);
  }

  public List<OrderEntity> getOrdersByCustomers(CustomerEntity customerEntity)
          throws AuthorizationFailedException {
    List<OrderEntity> orderDetails = orderDao.getAllOrderByCustomerId(customerEntity.getId());
    if (orderDetails == null) {
      throw new NoResultException("Order Details Not Found for CustomerId");
    }

    return orderDetails;
  }


  public List<OrderItemEntity> getAllItemsByOrder(Integer orderId) {
    return orderDao.getItemsByOrder(orderId);
  }

  public AddressEntity validateAddress(String uuid) throws AddressNotFoundException {
    AddressEntity address = orderDao.getAddressByUUId(uuid);
    if (address == null) {
      throw new AddressNotFoundException("ANF-003", "No address by this id");
    }

    return address;
  }

  private CustomerEntity retrieveCustomerId(String accessToken) {
    CustomerEntity customerDetails =
        orderDao.getCustomerAuthEntityByAccessToken(accessToken).getCustomer();
    return customerDetails;
  }

  public CustomerEntity validateCustomerAuth(String accessToken)
      throws AuthorizationFailedException {
    CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(accessToken);
    if (customerAuthEntity == null) {
      throw new AuthorizationFailedException(ATHR_001, CUSTOMER_IS_NOT_LOGGED_IN);
    }
    if (customerAuthEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(ATHR_002, CUSTOMER_IS_LOGGED_OUT);
    }
    final ZonedDateTime now = ZonedDateTime.now();
    if (now.isAfter(customerAuthEntity.getExpiresAt())) {
      throw new AuthorizationFailedException(ATHR_003, SESSION_IS_EXPIRED);
    }
    return customerAuthEntity.getCustomer();
  }

  private void ValidateCoupon(String couponName) throws CouponNotFoundException {
    if (couponName == null) {
      throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
    }
  }
}
