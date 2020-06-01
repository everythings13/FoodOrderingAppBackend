package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/** @Author : Harika Etamukkala OrderController */
@CrossOrigin
@RestController
public class OrderController {

  private final String BEARER = "Bearer";

  // Service class for order details
  @Autowired private OrderService orderService;
  @Autowired private CustomerService customerService;
  @Autowired private PaymentService paymentService;
  @Autowired private RestaurantService restaurantService;
  @Autowired private AddressService addressService;
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
    String token = getToken(accessToken);
    customerService.getCustomer(token);
    if (couponName == null) {
      throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
    }
    OrderListCoupon couponResponse = getOrderCouponResponse(couponName);
    ResponseEntity<OrderListCoupon> response = new ResponseEntity<>(couponResponse, HttpStatus.OK);
    return response;
  }

  /**
   * This method is used to get all orders of a customer
   *
   * @param accessToken
   * @param accessToken
   * @return
   * @throws AuthorizationFailedException
   * @throws CouponNotFoundException
   */
  @RequestMapping(method = RequestMethod.GET, path = "/order")
  public ResponseEntity<CustomerOrderResponse> getAllOrder(
      @RequestHeader("authorization") final String accessToken)
      throws AuthorizationFailedException, CouponNotFoundException {

    String token = getToken(accessToken);
    CustomerEntity customerEntity = customerService.getCustomer(token);

    List<OrderEntity> orderList = orderService.getOrdersByCustomers(customerEntity.getUuid());
    List<OrderList> orders = new ArrayList<>();

    for (OrderEntity orderEntity : orderList) {
      OrderList order = convertToOrderList(orderEntity);
      List<OrderItemEntity> items = orderService.getAllItemsByOrder(orderEntity.getId());
      List<ItemQuantityResponse> itemQuantities = items.stream().map(itemsEntity -> {
            ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();
            ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem();
            itemQuantityResponseItem.setItemName(itemsEntity.getItemId().getItemName());
            itemQuantityResponseItem.setId(UUID.fromString(itemsEntity.getItemId().getUuid()));
            itemQuantityResponseItem.setItemPrice(itemsEntity.getItemId().getPrice());
            itemQuantityResponseItem.setType(
                ItemQuantityResponseItem.TypeEnum.valueOf(
                    itemsEntity.getItemId().getType().toString()));
            itemQuantityResponse.setItem(itemQuantityResponseItem);
            return itemQuantityResponse;
          }).collect(Collectors.toList());
      order.setItemQuantities(itemQuantities);
      orders.add(order);
    }

    CustomerOrderResponse response = new CustomerOrderResponse();
    response.setOrders(orders);

    return new ResponseEntity<>(response, HttpStatus.OK);
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
          RestaurantNotFoundException, AddressNotFoundException, ItemNotFoundException {
    String token = getToken(accessToken);
    CustomerEntity customer = customerService.getCustomer(token);

    OrderEntity order = new OrderEntity();
    if (customer != null) {
      OrderEntity orderEntity = getOrderListEntity(orderRequest, customer);
      order = orderService.saveOrder(orderEntity);
      OrderEntity finalOrder = order;
      orderRequest.getItemQuantities()
          .forEach(
              itemQuantity -> {
                OrderItemEntity orderItemEntity = new OrderItemEntity();
                orderItemEntity.setQuantity(itemQuantity.getQuantity());
                orderItemEntity.setPrice(itemQuantity.getPrice());
                ItemsEntity entity = new ItemsEntity();
                entity.setUuid(itemQuantity.getItemId().toString());
                orderItemEntity.setItemId(entity);
                orderItemEntity.setOrderId(finalOrder);
                orderService.saveOrderItem(orderItemEntity);
              });
    }
    SaveAddressResponse response =
        new SaveAddressResponse()
            .id(String.valueOf(order.getUuid()))
            .status("ORDER SUCCESSFULLY PLACED");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  private OrderEntity getOrderListEntity(
      @RequestBody(required = true) SaveOrderRequest orderRequest, CustomerEntity customer)
      throws PaymentMethodNotFoundException, CouponNotFoundException, AuthorizationFailedException,
          ItemNotFoundException, AddressNotFoundException, RestaurantNotFoundException {
    OrderEntity order = new OrderEntity();
    order.setBill(orderRequest.getBill().doubleValue());
    order.setUuid(UUID.randomUUID().toString());
    order.setDiscount(orderRequest.getDiscount().doubleValue());
    order.setDate(new Timestamp(new Date().getTime()));
    order.setCustomer(customer);
    CouponEntity couponEntity =
            orderService.getCouponByCouponName(orderRequest.getCouponId().toString());
    order.setCoupon(couponEntity);
    PaymentEntity paymentDetails =
            paymentService.getPaymentByUUID(orderRequest.getPaymentId().toString());
    order.setPayment(paymentDetails);
    AddressEntity addressEntity = addressService.getAddressByUUID(orderRequest.getAddressId(),customer);;
    //addressEntity.setUuid(orderRequest.getAddressId());
    order.setAddress(addressEntity);


    Restaurant restaurantEntity =
        restaurantService.getRestaurantByRestaurantUuid(orderRequest.getRestaurantId().toString());
    order.setRestaurant(restaurantEntity);

    return order;
  }

  private OrderItemEntity getOrderItemEntity(ItemQuantity itemQuantity) {
    OrderItemEntity orderItemEntity = new OrderItemEntity();
    orderItemEntity.setQuantity(itemQuantity.getQuantity());
    orderItemEntity.setPrice(itemQuantity.getPrice());
    ItemsEntity entity = new ItemsEntity();
    entity.setUuid(itemQuantity.getItemId().toString());
    entity.setPrice(itemQuantity.getPrice());
    orderItemEntity.setItemId(entity);
    return orderItemEntity;
  }

  private OrderListCoupon getOrderCouponResponse(@PathVariable("coupon_name") String couponName)
      throws CouponNotFoundException, AuthorizationFailedException {
    CouponEntity couponCodeDetails = orderService.getCouponByCouponName(couponName);
    OrderListCoupon couponResponse = new OrderListCoupon();
    couponResponse.setId(UUID.fromString(couponCodeDetails.getUuid()));
    couponResponse.setCouponName(couponCodeDetails.getCouponName());
    couponResponse.setPercent(couponCodeDetails.getPercent());
    return couponResponse;
  }

  private OrderList convertToOrderList(OrderEntity orderEntity) {
    OrderList order = new OrderList();
    order.setDiscount(BigDecimal.valueOf(orderEntity.getDiscount()));
    order.setDate(orderEntity.getDate().toString());
    order.setId(UUID.fromString(orderEntity.getUuid()));
    order.setBill(BigDecimal.valueOf(orderEntity.getBill()));

    OrderListAddress address = new OrderListAddress();
    BeanUtils.copyProperties(orderEntity.getAddress(), address);
    address.setId(UUID.fromString(orderEntity.getAddress().getUuid()));
    order.setAddress(address);
    OrderListCoupon coupon = new OrderListCoupon();
    BeanUtils.copyProperties(orderEntity.getCoupon(), coupon);
    coupon.setCouponName(orderEntity.getCoupon().getCouponName());
    coupon.setId(UUID.fromString(orderEntity.getCoupon().getUuid()));
    OrderListPayment payment = new OrderListPayment();
    BeanUtils.copyProperties(orderEntity.getPayment(), payment);
    payment.setId(UUID.fromString(orderEntity.getPayment().getUuid()));
    OrderListCustomer customer = new OrderListCustomer();
    BeanUtils.copyProperties(customer, orderEntity.getCustomer());
    customer.setId(UUID.fromString(orderEntity.getCustomer().getUuid()));
    order.setAddress(address);
    order.setPayment(payment);
    order.setCoupon(coupon);
    order.setCustomer(customer);
    return order;
  }

  private List<OrderList> convertToResponse(List<OrderEntity> orderList) {
    List<OrderList> response =
        orderList.stream().map(this::convertToOrderList).collect(Collectors.toList());
    return response;
  }

  private CustomerEntity validateCustomerAuth(String token) throws AuthorizationFailedException {
    CustomerEntity customerEntity = orderService.validateCustomerAuth(token);
    return customerEntity;
  }

  private String getToken(String accessToken) {
    return accessToken.replace(BEARER, "").trim();
  }
}
