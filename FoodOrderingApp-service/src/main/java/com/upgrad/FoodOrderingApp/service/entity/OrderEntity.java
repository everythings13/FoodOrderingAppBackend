package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.util.UUID;
@Entity
@Table(name = "Coupon")
public class OrderEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    @Column(name = "coupon_name")
    private String couponName ;
    @Column(name = "percent")
    private Integer percent ;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}

