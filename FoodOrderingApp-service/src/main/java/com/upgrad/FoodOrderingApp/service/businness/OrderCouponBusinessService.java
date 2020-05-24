package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderCouponDao;
import com.upgrad.FoodOrderingApp.service.entity.OrderCouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCouponBusinessService {

    @Autowired
    private OrderCouponDao orderCouponDao;

    public OrderCouponEntity getCouponDetails(String couponName) throws CouponNotFoundException {
       OrderCouponEntity orderCoupon= orderCouponDao.getOrderDetails(couponName);
       if (orderCoupon==null){
            throw  new CouponNotFoundException("CPF-001","No coupon by this name");
       }
        return orderCoupon;
    }
}
