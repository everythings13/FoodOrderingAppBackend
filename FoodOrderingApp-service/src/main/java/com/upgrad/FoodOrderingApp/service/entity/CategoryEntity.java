package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name = "category")
@NamedQueries({
  @NamedQuery(name = "getAllCategories", query = "SELECT c from CategoryEntity c ORDER BY c.categoryName"),
  @NamedQuery(
      name = "getCategoryByUUID",
      query = "SELECT c from CategoryEntity c WHERE c.uuid =:uuid ")
})
public class CategoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(columnDefinition = "CHAR(200)")
  private String uuid;

  @Column(name = "category_name")
  private String categoryName;
  @Transient
  private List<ItemsEntity> items;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public List<ItemsEntity> getItems() {
    return items;
  }

  public void setItems(List<ItemsEntity> items) {
    this.items = items;
  }
}
