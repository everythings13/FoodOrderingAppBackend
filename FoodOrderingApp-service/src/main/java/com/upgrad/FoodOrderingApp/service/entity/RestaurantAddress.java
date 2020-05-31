package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "address")
// @NamedQueries({
//  @NamedQuery(
//      name = "getRestaurantAddressById",
//      query = "SELECT a FROM RestaurantAddress a WHERE a.id =:id")
// })
public class RestaurantAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(columnDefinition = "CHAR(200)")
  private String uuid;

  @Column(name = "flat_buil_number")
  @Size(max = 255)
  private String flatNumber;

  @Column
  @Size(max = 255)
  private String locality;

  @Column
  @Size(max = 30)
  private String city;

  @Column
  @Size(max = 30)
  private String pincode;

  @ManyToOne
  @JoinColumn(name = "state_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private State state;

  @Column private Integer active;

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

  public String getFlatNumber() {
    return flatNumber;
  }

  public void setFlatNumber(String flatNumber) {
    this.flatNumber = flatNumber;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }
}
