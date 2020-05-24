package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.OrderListCoupon;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
    @RequestMapping(method = RequestMethod.GET,path = "/order/edit/{coupon_code}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OrderListCoupon coupon(@PathVariable("coupon_code") String couponCode, @RequestHeader("authorization") final String accessToken){
        //TODO -Authorization
        return null;

    }
}
