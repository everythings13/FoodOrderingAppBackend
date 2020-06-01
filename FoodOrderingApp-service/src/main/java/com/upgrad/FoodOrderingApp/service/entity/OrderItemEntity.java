package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@NamedQueries({
        @NamedQuery(name = "getItemsByOrderId",query = "SELECT oi from OrderItemEntity oi WHERE oi.orderId.id =:orderId")
})
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderId;
    @OneToOne
    @JoinColumn(name = "item_id")
    private ItemsEntity itemId;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "price")
    private Integer price;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderEntity getOrderId() {
        return orderId;
    }

    public void setOrderId(OrderEntity orderId) {
        this.orderId = orderId;
    }

    public ItemsEntity getItemId() {
        return itemId;
    }

    public void setItemId(ItemsEntity itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
