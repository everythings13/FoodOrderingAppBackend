package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NamedQueries({
  @NamedQuery(
      name = "getAllOrdersByCustomerId",
      query =
          "SELECT o FROM OrderEntity o WHERE o.customer.id =:customerId")
})
public class OrderEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "uuid")
  private String uuid;

  @Column(name = "bill")
  private BigDecimal bill;

  @Column(name = "discount")
  private BigDecimal discount;

  @Column(name = "date")
  private Timestamp date;

  @OneToOne
  @JoinColumn(name = "payment_id")
  private OrderPayment payment;

  @OneToOne
  @JoinColumn(name="address_id")
  private AddressEntity address;

  @OneToOne
  @JoinColumn(name = "restaurant_id")
  private OrderRestaurantEntity restaurant;

  @OneToOne
  @JoinColumn(name = "coupon_id")
  private OrderCouponEntity coupon;

  @OneToOne
  @JoinColumn(name="customer_id")
  private CustomerEntity customer;

  @Transient
  private List<OrderItemEntity> itemQuantities = null;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public BigDecimal getBill() {
    return bill;
  }

  public void setBill(BigDecimal bill) {
    this.bill = bill;
  }

  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(BigDecimal discount) {
    this.discount = discount;
  }

  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  public OrderPayment getPayment() {
    return payment;
  }

  public void setPayment(OrderPayment payment) {
    this.payment = payment;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(AddressEntity address) {
    this.address = address;
  }

  public OrderRestaurantEntity getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(OrderRestaurantEntity restaurant) {
    this.restaurant = restaurant;
  }

  public OrderCouponEntity getCoupon() {
    return coupon;
  }

  public void setCoupon(OrderCouponEntity coupon) {
    this.coupon = coupon;
  }

  public CustomerEntity getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerEntity customer) {
    this.customer = customer;
  }

  public List<OrderItemEntity> getItemQuantities() {
    return itemQuantities;
  }

  public void setItemQuantities(List<OrderItemEntity> itemQuantities) {
    this.itemQuantities = itemQuantities;
  }


}
