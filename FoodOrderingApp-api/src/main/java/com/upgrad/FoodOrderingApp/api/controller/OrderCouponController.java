package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.OrderList;
import com.upgrad.FoodOrderingApp.api.model.OrderListCoupon;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.service.businness.OrderCouponBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.OrderCouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderListEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class OrderCouponController {
    @Autowired
    private OrderCouponBusinessService orderCouponBusinessService;

    @RequestMapping(method = RequestMethod.GET,path = "/order/edit/{coupon_code}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<OrderListCoupon> coupon(@PathVariable("coupon_code") String couponCode, @RequestHeader("authorization") final String accessToken) throws CouponNotFoundException {
        //TODO -Authorization
        if(couponCode==null){
            throw  new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }
        OrderCouponEntity couponCodeDetails=orderCouponBusinessService.getCouponDetails(couponCode);
        OrderListCoupon couponResponse = new OrderListCoupon();
        couponResponse.setId(couponCodeDetails.getId());
        couponResponse.setCouponName(couponCodeDetails.getCouponName());
        couponResponse.setPercent(couponCodeDetails.getPercent());
        ResponseEntity<OrderListCoupon> response = new ResponseEntity<>(couponResponse, HttpStatus.OK);
        return response;

    }
    @RequestMapping(method = RequestMethod.POST,path = "/order",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity save(@RequestBody (required = true)SaveOrderRequest orderRequest){
        //TODO -set all values
        OrderListEntity order = new OrderListEntity();
        order.setBill(orderRequest.getBill());
        long orderId=orderCouponBusinessService.saveOrder(order);
        SaveAddressResponse response = new SaveAddressResponse().id(String.valueOf(orderId)).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
}
