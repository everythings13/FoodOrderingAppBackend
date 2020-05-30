package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@NamedQueries({
  @NamedQuery(
      name = "getAllOrdersByCustomerId",
      query = "SELECT o FROM OrderListEntity o WHERE o.customerId =:customerId ORDER BY date desc ")
})
public class OrderListEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "bill")
  private BigDecimal bill;

  @Column(name = "discount")
  private long discount;

  @Column(name = "date")
  private String date;

  @Column(name = "payment_id")
  private String paymentId;

  @Column(name = "address_id")
  private String addressId;

  @Column(name = "restaurant_id")
  private String restaurantId;

  @Column(name = "coupon_id")
  private String couponId;

  @Column(name = "customer_id")
  private String customerId;

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

  public String getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(String paymentId) {
    this.paymentId = paymentId;
  }

  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }

  public String getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(String restaurantId) {
    this.restaurantId = restaurantId;
  }

  public String getCouponId() {
    return couponId;
  }

  public void setCouponId(String couponId) {
    this.couponId = couponId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
}
