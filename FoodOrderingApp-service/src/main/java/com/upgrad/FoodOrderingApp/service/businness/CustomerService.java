package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class
CustomerService {

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

@Transactional(propagation = Propagation.REQUIRED)
public CustomerAuthEntity authenticate(String email, String password){
    CustomerAuthEntity customerAuthToken = new CustomerAuthEntity();
    CustomerEntity customerEntity = customerDao.getCustomerByEmail(email);
    if (customerEntity != null) {
        String encryptedPassword = cryptographyProvider.encrypt(customerEntity.getSalt(), password);
        if (customerEntity.getPassword().equalsIgnoreCase(encryptedPassword)) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(customerEntity.getPassword());
            customerAuthToken.setCustomer(customerEntity);

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthToken.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
            customerAuthToken.setUuid(UUID.randomUUID().toString());
            customerAuthToken.setLoginAt(now);
            customerAuthToken.setExpiresAt(expiresAt);
            customerDao.login(customerAuthToken);
        }
    }
    return customerAuthToken;
}

    public CustomerEntity getUserProfile(String userId, String token) {
        CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(token);
        CustomerEntity customerEntity = customerDao.getCustomerByUUID(userId);
        if (customerEntity.getUuid().equalsIgnoreCase(customerAuthEntity.getCustomer().getUuid())) {
            return customerEntity;
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(String token){
      CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(token);
      final ZonedDateTime now = ZonedDateTime.now();
      customerAuthEntity.setLogoutAt(now);
      return customerDao.logout(customerAuthEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(String token, String firstName, String lastName)
    {
        CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(token);
        CustomerEntity customerEntity = customerAuthEntity.getCustomer();
        customerEntity.setFirstname(firstName);
        customerEntity.setLastname((lastName));
        return customerDao.updateCustomer(customerEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updatePassword(String token, String old_password, String new_password)
    {
        CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(token);
        CustomerEntity customerEntity = customerAuthEntity.getCustomer();
        String oldEncryptedPassword = cryptographyProvider.encrypt(customerEntity.getSalt(), old_password);
        String newEncryptedPassword = cryptographyProvider.encrypt(customerEntity.getSalt(), new_password);

        if(old_password != null && new_password != null && oldEncryptedPassword.equalsIgnoreCase(customerEntity.getPassword())) {
            customerEntity.setPassword(newEncryptedPassword);
        }
        return customerDao.updatePassword(customerEntity);
    }
}

