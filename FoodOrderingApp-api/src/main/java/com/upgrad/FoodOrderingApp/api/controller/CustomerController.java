package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping(method = RequestMethod.POST,path = "/customer/signup", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signUpUser(@RequestBody SignupCustomerRequest signUpCustomerRequest) throws SignUpRestrictedException
    {
        CustomerEntity customerEntity =new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstname(signUpCustomerRequest.getFirstName());
        customerEntity.setPassword(signUpCustomerRequest.getPassword());
        customerEntity.setContactnumber(signUpCustomerRequest.getContactNumber());
        customerEntity.setEmail(signUpCustomerRequest.getEmailAddress());
        customerEntity.setLastname(signUpCustomerRequest.getLastName());
        customerEntity.setSalt("1234abc");
        CustomerEntity createdCustomerEntity= customerService.createCustomer(customerEntity);
        SignupCustomerResponse signupUserResponse=new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(signupUserResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("Authorization") String authorization)
    {
        String[] credentials= new String(Base64.getDecoder().decode(authorization.split("Basic ")[1])).split(":");
        CustomerAuthEntity customerAuthEntity=customerService.authenticate(credentials[0],credentials[1]);
        LoginResponse loginResponse=new LoginResponse().id(customerAuthEntity.getUuid()).message("LOGGED IN SUCCESSFULLY");
        loginResponse.setFirstName(customerAuthEntity.getCustomer().getFirstname());
        loginResponse.setLastName(customerAuthEntity.getCustomer().getLastname());
        loginResponse.setContactNumber(customerAuthEntity.getCustomer().getContactnumber());
        loginResponse.setEmailAddress(customerAuthEntity.getCustomer().getEmail());
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", customerAuthEntity.getAccessToken());
        return new ResponseEntity<LoginResponse>(loginResponse,headers,HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, path =  "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        CustomerAuthEntity customerAuthEntity=customerService.logout(token);
        LogoutResponse logoutResponse=new LogoutResponse().id(customerAuthEntity.getCustomer().getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestBody UpdateCustomerRequest updateCustomerRequest, @RequestHeader
    ("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.updateCustomer(token, updateCustomerRequest.getFirstName(), updateCustomerRequest.getLastName());
        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id(customerEntity.getUuid()).firstName(customerEntity.getFirstname()).lastName(customerEntity.getLastname()).
                status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse,HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest, @RequestHeader
            ("Authorization") String authorization)
    {
        String token = authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.updatePassword(token, updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword());

        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id(customerEntity.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse,HttpStatus.OK);

    }
}
//Controller->service-> Dao