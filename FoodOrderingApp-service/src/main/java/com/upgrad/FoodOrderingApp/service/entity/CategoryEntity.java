package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = "getAllCategories" ,query = "SELECT c from CategoryEntity c "),
        @NamedQuery(name = "getCategoryById" ,query = "SELECT c from CategoryEntity c WHERE c.id =:id ")
})
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    @Column(name = "category_name")
    private String categoryName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
