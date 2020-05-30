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
    private BigInteger id;
    @Column(name = "item_id")
    private BigInteger itemId;
    @Column(name = "category_id")
    private BigInteger categoryId;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getItemId() {
        return itemId;
    }

    public void setItemId(BigInteger itemId) {
        this.itemId = itemId;
    }

    public BigInteger getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(BigInteger categoryId) {
        this.categoryId = categoryId;
    }
}
