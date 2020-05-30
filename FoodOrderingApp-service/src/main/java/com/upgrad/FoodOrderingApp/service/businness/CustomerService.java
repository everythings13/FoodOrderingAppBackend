package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.regex.Pattern;


@Service
public class
CustomerService {

@Autowired
CustomerDao customerDao;

@Autowired
PasswordCryptographyProvider cryptographyProvider;

@Transactional(propagation = Propagation.REQUIRED)
public CustomerEntity createCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException
{
    if(customerEntity.getFirstname() == null || customerEntity.getFirstname()== "" || customerEntity.getPassword() == null || customerEntity.getPassword() == ""|| customerEntity.getEmail() == null || customerEntity.getEmail() == ""||customerEntity.getContactnumber() == null ||customerEntity.getContactnumber() == "")
        throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled)");
    boolean emailMatch= Pattern.matches("(^[A-Za-z0-9+_.-]+@(.+)$)",customerEntity.getEmail());
    if (!emailMatch) {
        throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
    }
    if(customerEntity.getContactnumber().length()== 10){
        boolean matched = Pattern.matches("(^[0-9]*$)",customerEntity.getContactnumber());
        if(!matched){
            throw new SignUpRestrictedException("SGR-003","Invalid contact number!");
        }
    }else {throw new SignUpRestrictedException("SGR-003","Invalid contact number!");}

    boolean passwordMatch = Pattern.matches("(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[[#@$%&*!^]])(?=\\S+$).{8,}$)",customerEntity.getPassword());
    if (!passwordMatch) {
        throw new SignUpRestrictedException("SGR-004", "Weak password!");
    }

    CustomerEntity existingCustomerEntity = customerDao.getCustomerByContactNumber(customerEntity.getContactnumber());
    if(existingCustomerEntity!=null) {
        throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
    }
    String encryptedPassword = cryptographyProvider.encrypt(customerEntity.getSalt(), customerEntity.getPassword());
    customerEntity.setPassword(encryptedPassword);
    return customerDao.createCustomer(customerEntity);
}


@Transactional(propagation = Propagation.REQUIRED)
public CustomerAuthEntity authenticate(String contactnumber, String password) throws AuthenticationFailedException {
    CustomerAuthEntity customerAuthToken = new CustomerAuthEntity();
    CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(contactnumber);
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
        else
        {
            throw new AuthenticationFailedException("ATH-002" , "Invalid Credentials");
        }
    }
    else
        {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
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
    public CustomerAuthEntity logout(String token) throws AuthorizationFailedException {
      CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(token);
      if(customerAuthEntity == null){
          throw new AuthorizationFailedException("ATHR-001" ,"Customer is not Logged in.");
      }
      if(customerAuthEntity.getLogoutAt() != null)
      {
          throw new AuthorizationFailedException("ATHR-002" , "Customer is logged out. Log in again to access this endpoint.");
      }
      final ZonedDateTime now = ZonedDateTime.now();
      if(now.getSecond() > customerAuthEntity.getExpiresAt().getSecond())
      {
          throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
      }
      customerAuthEntity.setLogoutAt(now);
      return customerDao.logout(customerAuthEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(String token, String firstName, String lastName) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(token);
        if(customerAuthEntity.getAccessToken() == null || customerAuthEntity.getAccessToken().isEmpty()) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt() != null)
        {
            throw new AuthorizationFailedException("ATHR-002" , "Customer is logged out. Log in again to access this endpoint.");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        if(now.getSecond() > customerAuthEntity.getExpiresAt().getSecond())
        {
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }
        CustomerEntity customerEntity = customerAuthEntity.getCustomer();
        customerEntity.setFirstname(firstName);
        customerEntity.setLastname((lastName));
        return customerDao.updateCustomer(customerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updatePassword(String token, String old_password, String new_password) throws UpdateCustomerException, AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(token);
        CustomerEntity customerEntity = customerAuthEntity.getCustomer();
        if(old_password == null || old_password.isEmpty() || new_password == null || new_password.isEmpty()) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        if(customerAuthEntity.getAccessToken() == null || customerAuthEntity.getAccessToken().isEmpty()) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt() != null)
        {
            throw new AuthorizationFailedException("ATHR-002" , "Customer is logged out. Log in again to access this endpoint.");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        if(now.getSecond() > customerAuthEntity.getExpiresAt().getSecond())
        {
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        }
        String oldEncryptedPassword = cryptographyProvider.encrypt(customerEntity.getSalt(), old_password);
        String newEncryptedPassword = cryptographyProvider.encrypt(customerEntity.getSalt(), new_password);

        boolean passwordMatch = Pattern.matches("(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#@$%&*!^])(?=\\S+$).{8,}$)",customerEntity.getPassword());
        if (!passwordMatch) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }

        if(!oldEncryptedPassword.equalsIgnoreCase(customerEntity.getPassword()))
        {
            throw new UpdateCustomerException("UCR-004","Incorrect old password!");
        }
        if(old_password != null && new_password != null && oldEncryptedPassword.equalsIgnoreCase(customerEntity.getPassword())) {
            customerEntity.setPassword(newEncryptedPassword);
        }
        return customerDao.updatePassword(customerEntity);
    }
}

