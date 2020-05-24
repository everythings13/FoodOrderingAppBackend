package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(name = "getAllOrders" ,query = "SELECT * FROM OrderListEntity ORDER BY date desc ")
})
public class OrderListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "bill")
    private BigDecimal bill;
    @Column(name="coupon_id")
    private OrderCouponEntity coupon;
    @Column(name = "discount")
    private long discount;
    @Column(name = "date")
    private String date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public OrderCouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(OrderCouponEntity coupon) {
        this.coupon = coupon;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
