package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

@Autowired
CustomerDao customerDao;

@Autowired
PasswordCryptographyProvider cryptographyProvider;

@Transactional(propagation = Propagation.REQUIRED)
public CustomerEntity createCustomer(CustomerEntity customerEntity){
    String encryptedPassword = cryptographyProvider.encrypt(customerEntity.getSalt(), customerEntity.getPassword());
    customerEntity.setPassword(encryptedPassword);
    return customerDao.createCustomer(customerEntity);
}
}

