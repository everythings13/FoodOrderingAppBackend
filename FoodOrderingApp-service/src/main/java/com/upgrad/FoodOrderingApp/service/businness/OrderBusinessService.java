package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderCouponDao;
import com.upgrad.FoodOrderingApp.service.entity.OrderCouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderListEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderBusinessService {

    @Autowired
    private OrderCouponDao orderCouponDao;

    public OrderCouponEntity getCouponDetails(String couponName) throws CouponNotFoundException {

        if(couponName==null){
            throw  new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }
       OrderCouponEntity orderCoupon= orderCouponDao.getOrderDetails(couponName);
       if (orderCoupon==null){
            throw  new CouponNotFoundException("CPF-001","No coupon by this name");
       }
        return orderCoupon;
    }

   public OrderCouponEntity getCouponDetailsByUUid(UUID uuid) throws CouponNotFoundException {
       OrderCouponEntity orderCouponDetails=orderCouponDao.getOrderDetailsById(uuid);
       if(orderCouponDetails==null){
           throw new CouponNotFoundException("CPF-002","No Coupon By this Id");
       }
     return orderCouponDetails;
   }

   public long saveOrder(OrderListEntity order){
       return orderCouponDao.saveOrder(order);
   }
}
