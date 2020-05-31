package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@NamedQueries({
  @NamedQuery(name = "getAll", query = "select r from Restaurant r order by r.customerRating desc"),
  @NamedQuery(
      name = "findByName",
      query =
          "select r from Restaurant r where lower(r.restaurantName) like :restaurantName order by r.restaurantName"),
})
public class Restaurant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(columnDefinition = "CHAR(200)")
  private String uuid;

  @Column(name = "restaurant_name")
  @Size(max = 50)
  private String restaurantName;

  @Column(name = "photo_URL")
  @Size(max = 255)
  private String photoURL;

  @Column(name = "customer_rating")
  private BigDecimal customerRating;

  @Column(name = "average_price_for_two")
  private Integer averagePrice;

  @Column(name = "number_of_customers_rated")
  private Integer numberCustomersRated;

  @ManyToOne
  @JoinColumn(name = "address_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private RestaurantAddress restaurantAddress;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "restaurant_category",
      joinColumns =
          @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false),
      inverseJoinColumns =
          @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false))
  private Set<Category> categories = new HashSet<>();

  public Restaurant() {}

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

  public String getRestaurantName() {
    return restaurantName;
  }

  public void setRestaurantName(String restaurantName) {
    this.restaurantName = restaurantName;
  }

  public String getPhotoURL() {
    return photoURL;
  }

  public void setPhotoURL(String photoURL) {
    this.photoURL = photoURL;
  }

  public BigDecimal getCustomerRating() {
    return customerRating;
  }

  public void setCustomerRating(BigDecimal customerRating) {
    this.customerRating = customerRating;
  }

  public Integer getAveragePrice() {
    return averagePrice;
  }

  public void setAveragePrice(Integer averagePrice) {
    this.averagePrice = averagePrice;
  }

  public Integer getNumberCustomersRated() {
    return numberCustomersRated;
  }

  public void setNumberCustomersRated(Integer numberCustomersRated) {
    this.numberCustomersRated = numberCustomersRated;
  }

  public RestaurantAddress getRestaurantAddress() {
    return restaurantAddress;
  }

  public void setRestaurantAddress(RestaurantAddress restaurantAddress) {
    this.restaurantAddress = restaurantAddress;
  }

  public Set<Category> getCategories() {
    return categories;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }
}
