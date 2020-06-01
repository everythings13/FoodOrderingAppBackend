package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@NamedQueries({
  @NamedQuery(
      name = "getAllOrdersByCustomerId",
      query =
          "SELECT o FROM OrderEntity o WHERE o.customer.id =:customerId"),
        @NamedQuery(name = "getOrdersByRestaurant",query = "SELECT o FROM OrderEntity o WHERE o.restaurant = :restaurant")
})
public class OrderEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "uuid")
  private String uuid;

  @Column(name = "bill")
  private Double bill;

  @Column(name = "discount")
  private Double discount;

  @Column(name = "date")
  private Date date;

  @OneToOne
  @JoinColumn(name = "payment_id")
  private PaymentEntity payment;

  @OneToOne
  @JoinColumn(name="address_id")
  private AddressEntity address;

  @OneToOne
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @OneToOne
  @JoinColumn(name = "coupon_id")
  private CouponEntity coupon;

  @OneToOne
  @JoinColumn(name="customer_id")
  private CustomerEntity customer;

  @Transient
  private List<OrderItemEntity> itemQuantities = null;

  public OrderEntity() {

  }

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

  public Double getBill() {
    return bill;
  }

  public void setBill(Double bill) {
    this.bill = bill;
  }

  public Double getDiscount() {
    return discount;
  }

  public void setDiscount(Double discount) {
    this.discount = discount;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public PaymentEntity getPayment() {
    return payment;
  }

  public void setPayment(PaymentEntity payment) {
    this.payment = payment;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(AddressEntity address) {
    this.address = address;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }

  public CouponEntity getCoupon() {
    return coupon;
  }

  public void setCoupon(CouponEntity coupon) {
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

  public OrderEntity(String uuid, Double bill, CouponEntity coupon, Double discount, Date date, PaymentEntity payment, AddressEntity address, Restaurant restaurant, CustomerEntity customer) {
    this.uuid = uuid;
    this.bill = bill;
    this.coupon = coupon;
    this.discount = discount;
    this.date = date;
    this.payment = payment;
    this.customer = customer;
    this.address = address;
    this.restaurant = restaurant;


  }
}
