package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "payment")
@NamedQueries({@NamedQuery(name = "getAllPaymentMethods", query = "select p from PaymentEntity p ")})
public class PaymentEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "payment_name")
  @NotNull
  @Size(max = 255)
  private String paymentName;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getPaymentName() {
    return paymentName;
  }

  public void setPaymentName(String paymentName) {
    this.paymentName = paymentName;
  }
}
