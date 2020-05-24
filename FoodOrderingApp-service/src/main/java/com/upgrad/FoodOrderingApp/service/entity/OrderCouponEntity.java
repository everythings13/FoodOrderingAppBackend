package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.util.UUID;
@Entity
@Table(name = "Coupon")
@NamedQueries({
        @NamedQuery(name="getCouponDetails",query = "SELECT o FROM OrderEntity o WHERE o.couponName =:couponName"),
        @NamedQuery(name="getCouponDetailsById",query = "SELECT o FROM OrderEntity o WHERE o.id =:id")
})
public class OrderCouponEntity {
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

