package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = "getAllCategories" ,query = "SELECT c from CategoryEntity c "),
        @NamedQuery(name = "getCategoryByUUID" ,query = "SELECT c from CategoryEntity c WHERE c.uuid =:uuid ")
})
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(200)")
    private String uuid;
    @Column(name = "category_name")
    private String categoryName;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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
}
