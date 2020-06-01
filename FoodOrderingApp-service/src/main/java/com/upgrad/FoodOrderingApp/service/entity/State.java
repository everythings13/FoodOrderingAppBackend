package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "state")
@NamedQueries({
        @NamedQuery(
                name = "getStateByUUID",
                query = "select ut from State ut where ut.uuid = :uuid "),
        @NamedQuery(
                name = "getAllStates",
                query = "select st from State st"
        )
})
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(200)")
    private String uuid;

    @Column(name = "stateName")
    @Size(max = 30)
    private String stateName;

    public State(String stateUuid, String stateName) {
        this.uuid = stateUuid;
        this.stateName = stateName;
    }

    public State() {
    }

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
