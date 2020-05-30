package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.OrderListCoupon;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.service.businness.OrderBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.OrderCouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderListEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @Autowired
    private OrderBusinessService orderCouponBusinessService;

    @RequestMapping(method = RequestMethod.GET,path = "/order/coupon/{coupon_name}")
    public ResponseEntity<OrderListCoupon> coupon(@PathVariable("coupon_name") String couponName, @RequestHeader("authorization") final String accessToken) throws CouponNotFoundException {
        //TODO -Authorization

        OrderCouponEntity couponCodeDetails=orderCouponBusinessService.getCouponDetails(couponName);
        OrderListCoupon couponResponse = new OrderListCoupon();
        couponResponse.setId(couponCodeDetails.getUuid());
        couponResponse.setCouponName(couponCodeDetails.getCouponName());
        couponResponse.setPercent(couponCodeDetails.getPercent());
        ResponseEntity<OrderListCoupon> response = new ResponseEntity<>(couponResponse, HttpStatus.OK);
        return response;

    }
    @RequestMapping(method = RequestMethod.POST,path = "/order",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity save(@RequestBody (required = true)SaveOrderRequest orderRequest) throws CouponNotFoundException {
        //TODO -set all values
        OrderListEntity order = new OrderListEntity();
        OrderCouponEntity couponDetails = orderCouponBusinessService.getCouponDetailsByUUid(orderRequest.getCouponId());
        if(couponDetails==null){
            throw new CouponNotFoundException("CPF-002","No coupon by this id");
        }
        order.setBill(orderRequest.getBill());
        order.setAddressId(orderRequest.getAddressId());
        order.setCoupon(orderRequest.getCouponId());
        order.setPaymentId(orderRequest.getPaymentId().toString());
        order.setRestaurantId(orderRequest.getRestaurantId().toString());

        long orderId=orderCouponBusinessService.saveOrder(order);
        SaveAddressResponse response = new SaveAddressResponse().id(String.valueOf(orderId)).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<>(response,HttpStatus.OK);

    }



}
