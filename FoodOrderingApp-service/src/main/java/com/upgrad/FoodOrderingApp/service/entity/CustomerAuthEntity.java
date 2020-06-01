package com.upgrad.FoodOrderingApp.service.entity;

<<<<<<< HEAD
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_auth")
@NamedQueries({
        @NamedQuery(
                name = "customerAuthByAccessToken",
                query="select ut from CustomerAuthEntity ut where ut.accessToken = :accessToken ")
})
public class CustomerAuthEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
=======
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;


@Entity
@Table(name = "customer_auth")
@NamedQueries(
        {
                @NamedQuery(name = "customerByAuthtoken", query = "select u from CustomerAuthEntity u where u.accessToken =:accessToken")
        }
)

public class CustomerAuthEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
>>>>>>> master
    @Size(max = 200)
    private String uuid;

    @ManyToOne
<<<<<<< HEAD
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CustomerEntity customerEntity;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "login_at")
    private LocalDateTime loginAt;

    @Column(name = "logout_at")
    private LocalDateTime logoutAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
=======
    @JoinColumn(name="customer_id")
    private CustomerEntity customer;

    @Column(name = "access_token")
    @Size(max = 500)
    private String accessToken;

    @Column(name = "login_at")
    private ZonedDateTime  loginAt;

    @Column(name = "logout_at")
    private ZonedDateTime logoutAt;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;
>>>>>>> master

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

<<<<<<< HEAD
    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
=======
    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
>>>>>>> master
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

<<<<<<< HEAD
    public LocalDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(LocalDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public LocalDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(LocalDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
=======
    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
>>>>>>> master
        this.expiresAt = expiresAt;
    }
}
