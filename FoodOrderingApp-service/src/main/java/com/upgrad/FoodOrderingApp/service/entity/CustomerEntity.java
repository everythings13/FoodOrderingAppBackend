package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name ="customer")
@NamedQueries(
        {
            @NamedQuery(name= "customerByUuid", query="select cu from CustomerEntity cu where cu.uuid = :uuid"),
                @NamedQuery(name= "customerById", query="select cu from CustomerEntity cu where cu.id = :id"),
                @NamedQuery(name= "customerByEmail", query="select cu from CustomerEntity cu where cu.email = :email"),
                @NamedQuery(name = "customerByContactNumber", query = "select cu from CustomerEntity cu where cu.contactnumber= :contactnumber")

        }
)
public class CustomerEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "FIRSTNAME")
    @NotNull
    @Size(max = 30)
    private String firstname;

    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 30)
    private String lastname;

    @Column(name = "EMAIL")
    @NotNull
    @Size(max = 50)
    private String email;

    @Column(name = "CONTACT_NUMBER")
    @Size(max = 30)
    private String contactnumber;

    @Column(name = "PASSWORD")
    @NotNull
    @Size(max = 255)
    private String password;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 255)
    private String salt;

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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


}
