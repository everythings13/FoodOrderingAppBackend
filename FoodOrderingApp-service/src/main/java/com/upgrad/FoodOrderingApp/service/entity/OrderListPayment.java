package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "payment")
@NamedQueries({
        @NamedQuery(
                name = "getPaymentByUUId",
                query = "Select p from OrderListPayment p where p.uuid = :uuid"
        ),
        @NamedQuery(
                name = "getPaymentById",
                query = "Select p from OrderListPayment p where p.id = :id"
        )}
)
public class OrderListPayment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "uuid")
  private String uuid;

  @Column(name = "payment_name")
  private String paymentName;

 /* @OneToOne(mappedBy = "payment")
  private OrderListEntity order;*/

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPaymentName() {
    return paymentName;
  }

  public void setPaymentName(String paymentName) {
    this.paymentName = paymentName;
  }

/*  public OrderListEntity getOrder() {
    return order;
  }

  public void setOrder(OrderListEntity order) {
    this.order = order;
  }*/

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
