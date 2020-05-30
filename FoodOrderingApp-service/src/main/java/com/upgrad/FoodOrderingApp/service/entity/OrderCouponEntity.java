package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.UUID;
@Entity
@Table(name = "Coupon")
@NamedQueries({
        @NamedQuery(name="getCouponDetails",query = "SELECT o FROM OrderCouponEntity o WHERE o.couponName =:couponName"),
        @NamedQuery(name="getCouponDetailsByUUId",query = "SELECT o FROM OrderCouponEntity o WHERE o.uuid =:uuid"),
        @NamedQuery(name="getCouponDetailsById",query = "SELECT o FROM OrderCouponEntity o WHERE o.id =:id")
})
public class OrderCouponEntity {
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

/*    @OneToOne(mappedBy = "coupon",cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private OrderListEntity order;*/

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
    public OrderListEntity getOrder() {
        return order;
    }

    public void setOrder(OrderListEntity order) {
        this.order = order;
    }*/
}

