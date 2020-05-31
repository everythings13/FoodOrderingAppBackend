package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@NamedQueries({
  @NamedQuery(
      name = "getCategoryAttributesByUuid",
      query = "SELECT c FROM Category c WHERE c.uuid =:uuid"),
})
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(columnDefinition = "CHAR(200)")
  private String uuid;

  @Column(name = "category_name")
  @Size(max = 255)
  private String categoryName;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "category_item",
      joinColumns =
          @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false),
      inverseJoinColumns =
          @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false))
  private List<Item> items = new ArrayList<>();

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

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public List<Item> getItemEntities() {
    return items;
  }
}
