package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.OrderBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderCouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderListEntity;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/** @Author : Harika Etamukkala OrderController */
@RestController
public class OrderController {
    //Service class for order details
  @Autowired private OrderBusinessService orderCouponBusinessService;

  /**
   * This Method is used to retrieve coupon details
   *
   * @param couponName
   * @param accessToken
   * @return OrderListCoupon
   * @throws CouponNotFoundException
   * @throws AuthorizationFailedException
   */
  @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}")
  public ResponseEntity<OrderListCoupon> coupon(
      @PathVariable("coupon_name") String couponName,
      @RequestHeader("authorization") final String accessToken)
      throws CouponNotFoundException, AuthorizationFailedException {
    // getting coupon details
    OrderListCoupon couponResponse = getOrderCouponResponse(couponName, accessToken);
    ResponseEntity<OrderListCoupon> response = new ResponseEntity<>(couponResponse, HttpStatus.OK);
    return response;
  }

  /**
   * This method is used to get all orders of a customer
   *
   * @param accessToken
   * @return
   * @throws AuthorizationFailedException
   * @throws CouponNotFoundException
   */
  @RequestMapping(method = RequestMethod.GET, path = "/order")
  public ResponseEntity<List<OrderList>> getAllOrder(
      @RequestHeader("authorization") final String accessToken)
      throws AuthorizationFailedException, CouponNotFoundException {
    List<OrderListEntity> orderList = orderCouponBusinessService.getAllOrders(accessToken);
    List<OrderList> response = convertToResponse(orderList);
    return new ResponseEntity<List<OrderList>>(response, HttpStatus.OK);
  }

  /**
   * This method is used to save order details
   *
   * @param orderRequest
   * @param accessToken
   * @return
   * @throws CouponNotFoundException
   * @throws PaymentMethodNotFoundException
   * @throws AuthorizationFailedException
   * @throws RestaurantNotFoundException
   * @throws AddressNotFoundException
   */
  @RequestMapping(
      method = RequestMethod.POST,
      path = "/order",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity save(
      @RequestBody(required = true) SaveOrderRequest orderRequest,
      @RequestHeader("authorization") final String accessToken)
      throws CouponNotFoundException, PaymentMethodNotFoundException, AuthorizationFailedException,
          RestaurantNotFoundException, AddressNotFoundException {
    OrderListEntity order = getOrderListEntity(orderRequest);
    long orderId = orderCouponBusinessService.saveOrder(order, accessToken);
    SaveAddressResponse response =
        new SaveAddressResponse().id(String.valueOf(orderId)).status("ORDER SUCCESSFULLY PLACED");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private OrderListEntity getOrderListEntity(
      @RequestBody(required = true) SaveOrderRequest orderRequest) {
    OrderListEntity order = new OrderListEntity();
    order.setBill(orderRequest.getBill());
    order.setUuid(UUID.randomUUID());
    order.setDiscount(orderRequest.getDiscount());
    order.setDate(new Timestamp(new Date().getTime()));
    order.setAddress(orderRequest.getAddressId());
    order.setCoupon(orderRequest.getCouponId().toString());
    order.setPayment(orderRequest.getPaymentId().toString());
    order.setRestaurant(orderRequest.getRestaurantId().toString());
    return order;
  }

  private OrderListCoupon getOrderCouponResponse(
      @PathVariable("coupon_name") String couponName,
      @RequestHeader("authorization") String accessToken)
      throws CouponNotFoundException, AuthorizationFailedException {
    OrderCouponEntity couponCodeDetails =
        orderCouponBusinessService.getCouponDetails(couponName, accessToken);
    OrderListCoupon couponResponse = new OrderListCoupon();
    couponResponse.setId(UUID.fromString(couponCodeDetails.getUuid()));
    couponResponse.setCouponName(couponCodeDetails.getCouponName());
    couponResponse.setPercent(couponCodeDetails.getPercent());
    return couponResponse;
  }

  private List<OrderList> convertToResponse(List<OrderListEntity> orderList) {
    List<OrderList> response = new ArrayList<>();
    if (orderList != null) {
      orderList.stream()
          .forEach(
              order -> {
                OrderList orderModel = new OrderList();
                orderModel.setId(order.getUuid());
                orderModel.setBill(order.getBill());
                orderModel.setDate(order.getDate().toString());
                orderModel.setDiscount(order.getDiscount());
                OrderCouponEntity couponEntity =
                    orderCouponBusinessService.getCouponDetailsById(order.getCouponId());
                OrderListCoupon coupon = new OrderListCoupon();
                BeanUtils.copyProperties(couponEntity, coupon);
                coupon.setId(UUID.fromString(couponEntity.getUuid()));
                OrderListAddress addressList = new OrderListAddress();
                AddressEntity addressEntity =
                    orderCouponBusinessService.getAddressDetailsById(order.getAddressId());
                BeanUtils.copyProperties(addressEntity, addressList);
                addressList.setId(UUID.fromString(addressEntity.getUuid()));
                OrderListPayment payment = new OrderListPayment();
                com.upgrad.FoodOrderingApp.service.entity.OrderListPayment paymentEntity =
                    orderCouponBusinessService.getPaymentDetailsById(order.getPaymentId());
                if (paymentEntity != null) {
                  BeanUtils.copyProperties(paymentEntity, payment);
                  payment.setId(UUID.fromString(paymentEntity.getUuid()));
                  orderModel.setPayment(payment);
                }
                OrderListCustomer customer = new OrderListCustomer();
                CustomerEntity customerEntity =
                    orderCouponBusinessService.getCustomerById(order.getCustomerId());
                BeanUtils.copyProperties(customerEntity, customer);
                customer.setId(UUID.fromString(couponEntity.getUuid()));
                orderModel.setCoupon(coupon);
                orderModel.setCustomer(customer);

                orderModel.setAddress(addressList);
                response.add(orderModel);
              });
    }
    return response;
  }
}
