package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "item")
@NamedQueries({
        @NamedQuery(name = "getItemsByIds",query = "SELECT it from ItemsEntity it WHERE it.id IN (:id)")
})
public class ItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "uuid")
    private UUID uuid;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "price")
    private Integer price;
    @Column(name = "type")
    private  String type;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
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
