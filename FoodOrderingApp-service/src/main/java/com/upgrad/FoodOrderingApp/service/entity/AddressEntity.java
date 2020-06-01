package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "address")
@NamedQueries({
        @NamedQuery(
                name = "getAddressByUUID",
                query = "Select ae from AddressEntity ae where ae.uuid = :uuid"
        )}
)

public class AddressEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;

    @Column(name = "flat_buil_number")
    private String flatBuildingName;

    @Column(name = "locality")
    private String locality;

    @Column(name = "city")
    private String city;

    @Column(name = "pincode")
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "state_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private State stateEntity;

    public AddressEntity(String addressId, String s, String someLocality, String someCity, String s1, State stateEntity) {
        this.uuid = addressId;
        this.flatBuildingName = s;
        this.locality = someLocality;
        this.city = someCity;
        this.pincode = s1;
        this.stateEntity = stateEntity;
    }

    public AddressEntity() {
    }

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlatBuilNo() {
        return flatBuildingName;
    }

    public void setFlatBuilNo(String flatBuildingName) {
        this.flatBuildingName = flatBuildingName;
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

    public State getStateEntity() {
        return stateEntity;
    }

    public void setState(State stateEntity) {
        this.stateEntity = stateEntity;
    }
}
