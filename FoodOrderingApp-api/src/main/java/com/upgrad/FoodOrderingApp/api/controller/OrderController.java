package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.*;
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
import java.util.stream.Collectors;

/**
 * @Author : Harika Etamukkala OrderController
 */
@RestController
public class OrderController {

    //Service class for order details
    @Autowired
    private OrderService orderCouponBusinessService;

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
        //orderCouponBusinessService.validateCustomerAuth(accessToken);
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
    public ResponseEntity<CustomerOrderResponse> getAllOrder(
            @RequestHeader("authorization") final String accessToken)
            throws AuthorizationFailedException, CouponNotFoundException {
        List<OrderEntity> orderList = orderCouponBusinessService.getAllOrders(accessToken);
        List<OrderList> orders = new ArrayList<>();

        List<ItemQuantityResponse> itemQuantities = new ArrayList<>();

        for(OrderEntity orderEntity: orderList) {
            OrderList order = convertToOrderList(orderEntity);
            List<OrderItemEntity> items = orderCouponBusinessService.getAllItemsByOrder(orderEntity.getId());
            items.stream().forEach(itemsEntity -> {
                ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();
                ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem();
                itemQuantityResponseItem.setItemName(itemsEntity.getItemId().getItemName());
                itemQuantityResponseItem.setId(UUID.fromString(itemsEntity.getItemId().getUuid()));
                itemQuantityResponseItem.setItemPrice(itemsEntity.getItemId().getPrice());
                ItemQuantityResponseItem.TypeEnum value = ItemQuantityResponseItem.TypeEnum.values()[Integer.valueOf(itemsEntity.getItemId().getType())];
                itemQuantityResponseItem.setType(value);
                itemQuantityResponse.setItem(itemQuantityResponseItem);
                itemQuantities.add(itemQuantityResponse);
            });
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
        // orderCouponBusinessService.validateCustomerAuth(accessToken);
        OrderEntity order = getOrderListEntity(orderRequest);
        OrderEntity orderId = orderCouponBusinessService.saveOrder(order);

        SaveAddressResponse response =
                new SaveAddressResponse().id(String.valueOf(orderId.getId())).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private OrderEntity getOrderListEntity(
            @RequestBody(required = true) SaveOrderRequest orderRequest) {
        OrderEntity order = new OrderEntity();
        order.setBill(orderRequest.getBill());
        order.setUuid(UUID.randomUUID().toString());
        order.setDiscount(orderRequest.getDiscount());
        order.setDate(new Timestamp(new Date().getTime()));
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUuid(orderRequest.getAddressId());

        OrderCouponEntity couponEntity = new OrderCouponEntity();
        couponEntity.setUuid(orderRequest.getCouponId().toString());
        order.setCoupon(couponEntity);
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setUuid(orderRequest.getPaymentId().toString());
        OrderRestaurantEntity restaurantEntity = new OrderRestaurantEntity();
        restaurantEntity.setUuid(orderRequest.getRestaurantId().toString());
        order.setAddress(addressEntity);
        order.setCoupon(couponEntity);
        order.setPayment(orderPayment);
        order.setRestaurant(restaurantEntity);

        List<OrderItemEntity> itemlist = new ArrayList<>();
        orderRequest.getItemQuantities().stream().forEach(itemQuantity -> {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setQuantity(itemQuantity.getQuantity());
            orderItemEntity.setPrice(itemQuantity.getPrice());
            ItemsEntity entity = new ItemsEntity();
            entity.setUuid(itemQuantity.getItemId().toString());
            entity.setPrice(itemQuantity.getPrice());
            orderItemEntity.setItemId(entity);
            itemlist.add(orderItemEntity);
        });
        order.setItemQuantities(itemlist);
        return order;
    }

    private OrderListCoupon getOrderCouponResponse(
            @PathVariable("coupon_name") String couponName,
            @RequestHeader("authorization") String accessToken)
            throws CouponNotFoundException, AuthorizationFailedException {
        OrderCouponEntity couponCodeDetails =
                orderCouponBusinessService.getCouponByCouponId(couponName);
        OrderListCoupon couponResponse = new OrderListCoupon();
        couponResponse.setId(UUID.fromString(couponCodeDetails.getUuid()));
        couponResponse.setCouponName(couponCodeDetails.getCouponName());
        couponResponse.setPercent(couponCodeDetails.getPercent());
        return couponResponse;
    }

    private OrderList convertToOrderList(OrderEntity orderEntity) {
        OrderList order = new OrderList();
        order.setDiscount(orderEntity.getDiscount());
        order.setDate(orderEntity.getDate().toString());
        order.setId(UUID.fromString(orderEntity.getUuid()));
        order.setBill(orderEntity.getBill());
        OrderListAddress address = new OrderListAddress();
        BeanUtils.copyProperties(orderEntity.getAddress(), address);
        address.setId(UUID.fromString(orderEntity.getAddress().getUuid()));
        order.setAddress(address);
        OrderListCoupon coupon = new OrderListCoupon();
        BeanUtils.copyProperties( orderEntity.getCoupon(),coupon);
        coupon.setCouponName(orderEntity.getCoupon().getCouponName());
        coupon.setId(UUID.fromString(orderEntity.getCoupon().getUuid()));
        OrderListPayment payment = new OrderListPayment();
        BeanUtils.copyProperties( orderEntity.getPayment(),payment);
        payment.setId(UUID.fromString(orderEntity.getPayment().getUuid()));
        OrderListCustomer customer = new OrderListCustomer();
        BeanUtils.copyProperties(customer, orderEntity.getCustomer());
        customer.setId(UUID.fromString(orderEntity.getUuid()));
        order.setAddress(address);
        order.setPayment(payment);
        order.setCoupon(coupon);
        order.setCustomer(customer);
        return order;
    }

    private List<OrderList> convertToResponse(List<OrderEntity> orderList) {
        List<OrderList> response = orderList.stream()
                .map(this::convertToOrderList)
                .collect(Collectors.toList());
        return response;
    }
}
