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
import com.upgrad.FoodOrderingApp.service.util.CommonUtil;
import static com.upgrad.FoodOrderingApp.service.util.MessageKeys.*;
@Service
public class
CustomerService {

@Autowired
CustomerDao customerDao;

@Autowired
PasswordCryptographyProvider cryptographyProvider;

@Transactional(propagation = Propagation.REQUIRED)
public CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException
{
    if(CommonUtil.customerEntityCheck(customerEntity))
    {
        throw new SignUpRestrictedException(SGR_005, EXCEPT_LASTNAME_ALL_FIELDS_SHOULD_BE_FILLED);
    }
    if (!CommonUtil.emailValidation(customerEntity.getEmail()))
    {
        throw new SignUpRestrictedException(SGR_002, INVALID_EMAILID_FORMAT);
    }
    if(!CommonUtil.contactNumberCheck(customerEntity.getContactnumber()))
     {
         throw new SignUpRestrictedException(SGR_003,INVALID_CONTACT_NUMBER);
     }
    if (!CommonUtil.passwordCheck(customerEntity.getPassword()))
    {
        throw new SignUpRestrictedException(SGR_004, WEAK_PASSWORD);
    }

    CustomerEntity existingCustomerEntity = customerDao.getCustomerByContactNumber(customerEntity.getContactnumber());
    if(existingCustomerEntity!=null)
    {
        throw new SignUpRestrictedException(SGR_001, THIS_CONTACT_NUMBER_IS_ALREADY_REGISTERED);
    }
    String encryptedPassword = cryptographyProvider.encrypt(customerEntity.getPassword(),customerEntity.getSalt());
    customerEntity.setPassword(encryptedPassword);
    return customerDao.saveCustomer(customerEntity);
}


@Transactional(propagation = Propagation.REQUIRED)
public CustomerAuthEntity authenticate(String contactnumber, String password) throws AuthenticationFailedException {
    CustomerAuthEntity customerAuthToken = new CustomerAuthEntity();
    CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(contactnumber);
    if (customerEntity != null) {
        String encryptedPassword = cryptographyProvider.encrypt(password,customerEntity.getSalt());
        if (customerEntity.getPassword().equalsIgnoreCase(encryptedPassword)) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(customerEntity.getPassword());
            customerAuthToken.setCustomer(customerEntity);

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthToken.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
            customerAuthToken.setUuid(UUID.randomUUID().toString());
            customerAuthToken.setLoginAt(now);
            customerAuthToken.setExpiresAt(expiresAt);
            customerDao.authenticate(customerAuthToken);
        }
        else
        {
            throw new AuthenticationFailedException(ATH_002 , INVALID_CREDENTIALS);
        }
    }
    else
        {
            throw new AuthenticationFailedException(ATH_001, THIS_CONTACT_NUMBER_IS_ALREADY_REGISTERED);
        }

    return customerAuthToken;
}

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(String token) throws AuthorizationFailedException {
      CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(token);
      final ZonedDateTime now = ZonedDateTime.now();
      customerAuthEntity.setLogoutAt(now);
      return customerDao.logout(customerAuthEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(CustomerEntity customerEntity) {

        return customerDao.updateCustomer(customerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(String old_password, String new_password, CustomerEntity customerEntity) throws UpdateCustomerException {

        String oldEncryptedPassword = cryptographyProvider.encrypt(old_password,customerEntity.getSalt());
        String newEncryptedPassword = cryptographyProvider.encrypt(new_password,customerEntity.getSalt());

        if (!CommonUtil.passwordCheck(new_password))
        {
            throw new UpdateCustomerException(UCR_001, WEAK_PASSWORD);
        }

        if(!oldEncryptedPassword.equalsIgnoreCase(customerEntity.getPassword()))
        {
            throw new UpdateCustomerException(UCR_004,INCORRECT_OLD_PASSWORD);
        }
        customerEntity.setPassword(newEncryptedPassword);
        return customerDao.updateCustomerPassword(customerEntity);
    }

    public CustomerEntity getCustomer(String token) throws AuthorizationFailedException
    {
        CustomerAuthEntity customerAuthEntity = customerDao.getUserByToken(token);
        if(customerAuthEntity == null) {
            throw new AuthorizationFailedException(ATHR_001, CUSTOMER_IS_NOT_LOGGED_IN);
        }
        if(customerAuthEntity.getLogoutAt() != null)
        {
            throw new AuthorizationFailedException(ATHR_002 , CUSTOMER_IS_LOGGED_OUT);
        }
        final ZonedDateTime now = ZonedDateTime.now();
        if(now.isAfter(customerAuthEntity.getExpiresAt()))
        {
            throw new AuthorizationFailedException(ATHR_003,SESSION_IS_EXPIRED);
        }
        return customerAuthEntity.getCustomer();
    }
}

