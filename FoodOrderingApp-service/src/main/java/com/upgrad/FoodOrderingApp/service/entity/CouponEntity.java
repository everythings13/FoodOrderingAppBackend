package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.UUID;
@Entity
@Table(name = "Coupon")
@NamedQueries({
        @NamedQuery(name="getCouponDetails",query = "SELECT o FROM CouponEntity o WHERE o.couponName =:couponName"),
        @NamedQuery(name="getCouponDetailsByUUId",query = "SELECT o FROM CouponEntity o WHERE o.uuid =:uuid"),
        @NamedQuery(name="getCouponDetailsById",query = "SELECT o FROM CouponEntity o WHERE o.id =:id")
})
public class CouponEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "coupon_name")
    private String couponName ;
    @Column(name = "percent")
    private Integer percent ;

    public CouponEntity(String uuid, String couponName, Integer percent) {
        this.uuid = uuid;
        this.couponName = couponName;
        this.percent = percent;
    }

    public CouponEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
/*
    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }*/

    @Override
    public String toString() {
        return "CouponEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", couponName='" + couponName + '\'' +
                ", percent=" + percent +
                '}';
    }
}

