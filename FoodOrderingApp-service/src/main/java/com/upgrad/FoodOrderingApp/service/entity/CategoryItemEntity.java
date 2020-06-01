package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "category_item")
@NamedQueries({
        @NamedQuery(name = "getItems",query = "SELECT ci FROM CategoryItemEntity ci where ci.categoryId =:categoryId")
})
public class CategoryItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "item_id")
    private Integer itemId;
    @Column(name = "category_id")
    private Integer categoryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
