package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import com.upgrad.FoodOrderingApp.service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

import static com.upgrad.FoodOrderingApp.service.util.MessageKeys.*;

@RestController
public class CustomerController {

  @Autowired CustomerService customerService;

  @RequestMapping(
      method = RequestMethod.POST,
      path = "/customer/signup",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupCustomerResponse> saveCustomer(
      @RequestBody SignupCustomerRequest signUpCustomerRequest) throws SignUpRestrictedException {
    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setUuid(UUID.randomUUID().toString());
    customerEntity.setFirstName(signUpCustomerRequest.getFirstName());
    customerEntity.setPassword(signUpCustomerRequest.getPassword());
    customerEntity.setContactnumber(signUpCustomerRequest.getContactNumber());
    customerEntity.setEmail(signUpCustomerRequest.getEmailAddress());
    customerEntity.setLastName(signUpCustomerRequest.getLastName());
    customerEntity.setSalt("1234abc");
    CustomerEntity createdCustomerEntity = customerService.saveCustomer(customerEntity);
    SignupCustomerResponse signupUserResponse =
        new SignupCustomerResponse()
            .id(createdCustomerEntity.getUuid())
            .status("CUSTOMER SUCCESSFULLY REGISTERED");
    return new ResponseEntity<SignupCustomerResponse>(signupUserResponse, HttpStatus.CREATED);
  }

  @RequestMapping(
      method = RequestMethod.POST,
      path = "/customer/login",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<LoginResponse> authenticate(
      @RequestHeader("authorization") String authorization) throws AuthenticationFailedException {
    if (!CommonUtil.basicAuthHeaderCheck(authorization)) {
      throw new AuthenticationFailedException(ATH_003, INCORRECT_FORMAT_DECODED_CREDENTIALS);
    }
    String[] credentials =
        new String(Base64.getDecoder().decode(authorization.split("Basic ")[1])).split(":");
    CustomerAuthEntity customerAuthEntity =
        customerService.authenticate(credentials[0], credentials[1]);
    LoginResponse loginResponse =
        new LoginResponse()
            .id(customerAuthEntity.getCustomer().getUuid())
            .message("LOGGED IN SUCCESSFULLY");
    loginResponse.setFirstName(customerAuthEntity.getCustomer().getFirstName());
    loginResponse.setLastName(customerAuthEntity.getCustomer().getLastName());
    loginResponse.setContactNumber(customerAuthEntity.getCustomer().getContactnumber());
    loginResponse.setEmailAddress(customerAuthEntity.getCustomer().getEmail());
    HttpHeaders headers = new HttpHeaders();
    headers.add("access-token", customerAuthEntity.getAccessToken());
    return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.POST,
      path = "/customer/logout",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, AuthenticationFailedException {
    if (!CommonUtil.tokenHeaderCheck(authorization)) {
      throw new AuthenticationFailedException(ATH_004, MISSING_INVALID_TOKEN);
    }
    String token = authorization.split("Bearer ")[1];
    CustomerEntity customerEntity = customerService.getCustomer(token);
    CustomerAuthEntity customerAuthEntity = customerService.logout(token);

    LogoutResponse logoutResponse =
        new LogoutResponse()
            .id(customerAuthEntity.getCustomer().getUuid())
            .message("LOGGED OUT SUCCESSFULLY");
    return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      path = "/customer",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UpdateCustomerResponse> updateCustomer(
      @RequestBody UpdateCustomerRequest updateCustomerRequest,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, UpdateCustomerException, AuthenticationFailedException {
    /*To create authorization header missing exception  for this*/
    if (!CommonUtil.tokenHeaderCheck(authorization)) {
      throw new AuthenticationFailedException(ATH_004, MISSING_INVALID_TOKEN);
    }
    if (CommonUtil.isNullOrEmpty(updateCustomerRequest.getFirstName())) {
      throw new UpdateCustomerException(UCR_002, FIRST_NAME_SHOULD_NOT_BE_EMPTY);
    }
    String token = authorization.split("Bearer ")[1];
    CustomerEntity customerEntity = customerService.getCustomer(token);
    customerEntity.setFirstName(updateCustomerRequest.getFirstName());
    customerEntity.setLastName(updateCustomerRequest.getLastName());
    customerEntity = customerService.updateCustomer(customerEntity);
    UpdateCustomerResponse updateCustomerResponse =
        new UpdateCustomerResponse()
            .id(customerEntity.getUuid())
            .firstName(customerEntity.getFirstName())
            .lastName(customerEntity.getLastName())
            .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
    return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      path = "/customer/password",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UpdatePasswordResponse> updateCustomerPassword(
      @RequestBody UpdatePasswordRequest updatePasswordRequest,
      @RequestHeader("authorization") String authorization)
      throws AuthorizationFailedException, UpdateCustomerException, AuthenticationFailedException {
    if (!CommonUtil.tokenHeaderCheck(authorization)) {
      throw new AuthenticationFailedException(ATH_004, MISSING_INVALID_TOKEN);
    }
    if (CommonUtil.isNullOrEmpty(updatePasswordRequest.getOldPassword())
        || CommonUtil.isNullOrEmpty(updatePasswordRequest.getNewPassword())) {
      throw new UpdateCustomerException(UCR_003, NO_FIELD_SHOULD_BE_EMPTY);
    }
    String token = authorization.split("Bearer ")[1];
    CustomerEntity customerEntity = customerService.getCustomer(token);
    customerEntity =
        customerService.updateCustomerPassword(
            updatePasswordRequest.getOldPassword(),
            updatePasswordRequest.getNewPassword(),
            customerEntity);

    UpdatePasswordResponse updatePasswordResponse =
        new UpdatePasswordResponse()
            .id(customerEntity.getUuid())
            .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
    return new ResponseEntity<>(updatePasswordResponse, HttpStatus.OK);
  }
}
