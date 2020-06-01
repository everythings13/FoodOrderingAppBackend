package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderCouponDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderCouponDao orderCouponDao;

    public OrderCouponEntity getCouponByCouponId(String couponName)
            throws CouponNotFoundException, AuthorizationFailedException {

        ValidateCoupon(couponName);
        OrderCouponEntity orderCoupon = orderCouponDao.getOrderDetails(couponName);
        if (orderCoupon == null) {
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }
        return orderCoupon;
    }

    public OrderCouponEntity getCouponDetailsByUUid(String uuid) throws CouponNotFoundException {
        OrderCouponEntity orderCouponDetails = orderCouponDao.getCouponDetailsByUUId(uuid);
        if (orderCouponDetails == null) {
            throw new CouponNotFoundException("CPF-001", "No Coupon By this Id");
        }
        return orderCouponDetails;
    }

    public OrderCouponEntity getCouponDetailsById(Integer id) {
        OrderCouponEntity orderCouponDetails = orderCouponDao.getCouponDetailsById(id);
        return orderCouponDetails;
    }

    public OrderPayment getPaymentDetailsById(Integer id) {
        OrderPayment paymentDetails = orderCouponDao.getPaymentById(id);
        return paymentDetails;
    }

    public CustomerEntity getCustomerById(Integer id) {
        CustomerEntity customerDeatils = orderCouponDao.getCustomerById(id);
        return customerDeatils;
    }

    public AddressEntity getAddressDetailsById(Integer id) {
        AddressEntity addressDetails = orderCouponDao.getAddressById(id);
        return addressDetails;
    }

    public OrderEntity saveOrder(OrderEntity order)
            throws AddressNotFoundException, AuthorizationFailedException, CouponNotFoundException,
            PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {
        //validateCustomerAuth(accessToken);

        OrderCouponEntity couponDetails = validateCoupon(order);
        OrderPayment paymentDetails = validatePayment(order);
        OrderRestaurantEntity restaurantDetails = validateRestaurant(order);
        AddressEntity addressDetails = validateAddress(order.getAddress().getUuid().toString());
        //validateAddress(order,address, accessToken);

        // order.setCustomer(retrieveCustomerId(accessToken));
        order.setCoupon(couponDetails);
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1);
        order.setCustomer(customerEntity);
        order.setRestaurant(restaurantDetails);
        order.setAddress(addressDetails);
        order.setPayment(paymentDetails);
        OrderEntity orderDetails = orderCouponDao.saveOrder(order);
        for(OrderItemEntity items:order.getItemQuantities()){
            ItemsEntity itemDetails = validateItems(items.getItemId().getUuid());
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            ItemsEntity itemsEntity = new ItemsEntity();
            itemsEntity.setUuid(items.getItemId().getUuid());
            itemsEntity.setId(itemDetails.getId());
            orderItemEntity.setItemId(itemsEntity);
            orderItemEntity.setOrderId(orderDetails);
            orderItemEntity.setPrice(items.getPrice());
            orderItemEntity.setQuantity(items.getQuantity());
            saveOrderItem(orderItemEntity);
        }

        return orderCouponDao.saveOrder(order);
    }

    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {

        OrderItemEntity orderItemDetails = orderCouponDao.saveOrderItems(orderItemEntity);

        return orderItemDetails;
    }


    private ItemsEntity validateItems
            (String itemId) throws ItemNotFoundException {
        ItemsEntity itemDetails = orderCouponDao.getItems(itemId);
        if (itemDetails == null) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        }
        return itemDetails;
    }

    private void validateAddress(OrderEntity order, AddressEntity address, String accessToken)
            throws AddressNotFoundException, AuthorizationFailedException {

        List<CustomerAddressEntity> addresses =
                orderCouponDao.getCustomerAddress(retrieveCustomerId(accessToken));
        if (!(addresses.contains(address))) {
            throw new AuthorizationFailedException(
                    "ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }
    }

    private OrderRestaurantEntity validateRestaurant(OrderEntity order)
            throws RestaurantNotFoundException {
        OrderRestaurantEntity restaurant =
                orderCouponDao.getRestaurantByUUId(order.getRestaurant().getUuid().toString());
        if (restaurant == null) {
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurant;
    }

    private OrderPayment validatePayment(OrderEntity order)
            throws PaymentMethodNotFoundException {
        OrderPayment payment = orderCouponDao.getPaymentByUUId(order.getPayment().getUuid().toString());
        if (payment == null) {
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        }
        return payment;
    }

    private OrderCouponEntity validateCoupon(OrderEntity order) throws CouponNotFoundException {
        OrderCouponEntity coupon = orderCouponDao.getCouponDetailsByUUId(order.getCoupon().getUuid());
        if (coupon == null) {
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        }

        return coupon;
    }

    public List<OrderEntity> getAllOrders(String accessToken)
            throws AuthorizationFailedException {
        // validateCustomerAuth(accessToken);
        // CustomerEntity customerDetails = retrieveCustomerId(accessToken);
    /*List<OrderEntity> orderDetails =
    orderCouponDao.getAllOrderByCustomerId(customerDetails.getUuid());*/
        List<OrderEntity> orderDetails = orderCouponDao.getAllOrderByCustomerId(1);
        if (orderDetails == null) {
            throw new NoResultException("Order Details Not Found for CustomerId");
        }

        return orderDetails;
    }

    public List<OrderItemEntity> getAllItemsByOrder(Integer orderId){
       return orderCouponDao.getItemsByOrder(orderId);
    }

    public AddressEntity validateAddress(String uuid) throws AddressNotFoundException {
        AddressEntity address = orderCouponDao.getAddressByUUId(uuid);
        if (address == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }

        return address;
    }

    private CustomerEntity retrieveCustomerId(String accessToken) {
        CustomerEntity customerDetails =
                orderCouponDao.getCustomerAuthEntityByAccessToken(accessToken).getCustomer();
        return customerDetails;
    }

    public void validateCustomerAuth(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity =
                orderCouponDao.getCustomerAuthEntityByAccessToken(accessToken);
        if (customerAuthEntity == null || customerAuthEntity.getAccessToken() == null) {
            throw new AuthorizationFailedException("ATHR_001", "Customer is not Logged in.");
        } else if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException(
                    "ATHR_002", "Customer is logged out. Log in again to access this endpoint.   ");
        } else if (customerAuthEntity.getExpiresAt().compareTo(ZonedDateTime.now()) < 0) {
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
