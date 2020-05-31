package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "restaurant_category")
@NamedQueries({
  @NamedQuery(
      name = "restaurantsByCategoryId",
      query = "select r from RestaurantCategory r where r.category.id=:id")
})
public class RestaurantCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "restaurant_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Restaurant restaurant;

  @ManyToOne
  @JoinColumn(name = "category_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Category category;

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Integer getId() {

    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
