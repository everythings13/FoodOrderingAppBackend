package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "item")
@NamedQueries({
        @NamedQuery(name = "getItemsByIds",query = "SELECT it from ItemsEntity it WHERE it.uuid =:uuid")
})
public class ItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "price")
    private Integer price;
    @Column(name = "type")
    private  String type;


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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
