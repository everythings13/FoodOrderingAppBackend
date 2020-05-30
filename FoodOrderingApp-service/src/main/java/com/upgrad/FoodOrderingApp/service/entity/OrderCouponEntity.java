package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.UUID;
@Entity
@Table(name = "Coupon")
@NamedQueries({
        @NamedQuery(name="getCouponDetails",query = "SELECT o FROM OrderCouponEntity o WHERE o.couponName =:couponName"),
        @NamedQuery(name="getCouponDetailsById",query = "SELECT o FROM OrderCouponEntity o WHERE o.id =:id")
})
public class OrderCouponEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "coupon_name")
    private String couponName ;
    @Column(name = "percent")
    private Integer percent ;


    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


}

