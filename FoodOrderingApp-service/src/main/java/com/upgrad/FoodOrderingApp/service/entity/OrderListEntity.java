package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NamedQueries({
  @NamedQuery(
      name = "getAllOrdersByCustomerId",
      query =
          "SELECT o FROM OrderListEntity o WHERE o.customerId =:customerId")
})
public class OrderListEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "uuid")
  private UUID uuid;

  @Column(name = "bill")
  private BigDecimal bill;

  @Column(name = "discount")
  private BigDecimal discount;

  @Column(name = "date")
  private Timestamp date;
  @Column(name = "payment_id" ,updatable = false,insertable = true)
  private Integer paymentId;
  @Column(name="address_id")
  private Integer addressId;
  @Column(name = "restaurant_id")
  private Integer restaurantId;
  @Column(name = "coupon_id")
  private Integer couponId;
  @Column(name="customer_id")
  private Integer customerId;
  @Transient
  private String payment;
  @Transient
  private String restaurant;
  @Transient
  private String address;
  @Transient
  private String coupon;




  public String getPayment() {
    return payment;
  }

  public void setPayment(String payment) {
    this.payment = payment;
  }

  public String getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(String restaurant) {
    this.restaurant = restaurant;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCoupon() {
    return coupon;
  }

  public void setCoupon(String coupon) {
    this.coupon = coupon;
  }
  /*
  @JoinColumn(name = "payment_id",nullable = false)
  @OneToOne(fetch = FetchType.LAZY, optional = false,cascade=CascadeType.ALL)
  private OrderListPayment payment;

  @JoinColumn(name = "address_id",nullable = false)
  @OneToOne(fetch = FetchType.LAZY, optional = false,cascade=CascadeType.ALL)
  private AddressEntity address;

  @OneToOne(fetch = FetchType.LAZY, optional = false,cascade=CascadeType.ALL)
  @JoinColumn(name = "restaurant_id",nullable = false)
  private OrderRestaurantEntity restaurant;

  @OneToOne(fetch = FetchType.LAZY, optional = false,cascade=CascadeType.ALL)
  @JoinColumn(name = "coupon_id",nullable = true)
  private OrderCouponEntity coupon;

  @OneToOne(fetch = FetchType.LAZY, optional = false,cascade=CascadeType.ALL)
  @JoinColumn(name = "customer_id",nullable = false )
  private CustomerEntity customer;*/

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
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

  /*public OrderListPayment getPayment() {
    return payment;
  }

  public void setPayment(OrderListPayment payment) {
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
*/

  public Integer getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(Integer paymentId) {
    this.paymentId = paymentId;
  }

  public Integer getAddressId() {
    return addressId;
  }

  public void setAddressId(Integer addressId) {
    this.addressId = addressId;
  }

  public Integer getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(Integer restaurantId) {
    this.restaurantId = restaurantId;
  }

  public Integer getCouponId() {
    return couponId;
  }

  public void setCouponId(Integer couponId) {
    this.couponId = couponId;
  }

  public Integer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  public AddressEntity addAddress(String addressID){
    AddressEntity entity = new AddressEntity();
    entity.setUuid(addressID);
      return entity;
  }
  public OrderCouponEntity addCoupon(String couponId){
    OrderCouponEntity entity = new OrderCouponEntity();
    entity.setUuid(couponId);
    return entity;
  }
  public OrderListPayment addPayment(UUID paymentId){
    OrderListPayment entity = new OrderListPayment();
    entity.setUuid(paymentId.toString());
    return entity;
  }
  public OrderRestaurantEntity addRestaurant(String restaurantId){
    OrderRestaurantEntity entity = new OrderRestaurantEntity();
    entity.setUuid(restaurantId);
    return entity;
  }

  public CustomerEntity addCustomer(String customerId){
    CustomerEntity entity = new CustomerEntity();
    entity.setUuid(customerId);
    return entity;
  }
}
