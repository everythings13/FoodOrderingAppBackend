package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "restaurant")
@NamedQueries({
        @NamedQuery(
                name = "getRestaurantByUUId",
                query = "SELECT r FROM OrderRestaurantEntity r where r.uuid = :uuid"
        ),
        @NamedQuery(
                name = "getRestaurantById",
                query = "SELECT r FROM OrderRestaurantEntity r where r.id = :id"
        )}
)
public class OrderRestaurantEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigInteger id;

  @Column(name = "uuid")
  private String uuid;

  @Column(name = "restaurant_name")
  private String restaurantName;

  @Column(name = "photo_url")
  private String photo_url;

  @Column(name = "customer_rating")
  private Double customerRating;

  @Column(name = "average_price_for_two")
  private BigInteger avgPriceForTwo;

  @Column(name = "number_of_customers_rated")
  private BigInteger noOfCustomersRated;

  @Column(name = "address_id")
  private BigInteger addressId;

/*  @OneToOne(mappedBy = "restaurant")
  private OrderListEntity order;*/

  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getRestaurantName() {
    return restaurantName;
  }

  public void setRestaurantName(String restaurantName) {
    this.restaurantName = restaurantName;
  }

  public String getPhoto_url() {
    return photo_url;
  }

  public void setPhoto_url(String photo_url) {
    this.photo_url = photo_url;
  }

  public Double getCustomerRating() {
    return customerRating;
  }

  public void setCustomerRating(Double customerRating) {
    this.customerRating = customerRating;
  }

  public BigInteger getAvgPriceForTwo() {
    return avgPriceForTwo;
  }

  public void setAvgPriceForTwo(BigInteger avgPriceForTwo) {
    this.avgPriceForTwo = avgPriceForTwo;
  }

  public BigInteger getNoOfCustomersRated() {
    return noOfCustomersRated;
  }

  public void setNoOfCustomersRated(BigInteger noOfCustomersRated) {
    this.noOfCustomersRated = noOfCustomersRated;
  }

  public BigInteger getAddressId() {
    return addressId;
  }

  public void setAddressId(BigInteger addressId) {
    this.addressId = addressId;
  }

 /* public OrderListEntity getOrder() {
    return order;
  }

  public void setOrder(OrderListEntity order) {
    this.order = order;
  }*/
}
