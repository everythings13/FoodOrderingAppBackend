package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.State;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;


    public State getStateByUUID(String stateId)
    {
        try{
            return entityManager.createNamedQuery("getStateByUUID",State.class)
                    .setParameter("uuid",stateId).getSingleResult();
        }
        catch (NoResultException e)
        {
            return null;
        }
    }

    public List<State> getAllStates() {
        try{
            return entityManager.createNamedQuery("getAllStates",State.class)
                    .getResultList();
        }
        catch (NoResultException e)
        {
            return null;
        }
    }
}
