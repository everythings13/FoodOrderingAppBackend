package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
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
    public ResponseEntity<SignupCustomerResponse> signUpUser(@RequestBody SignupCustomerRequest signUpCustomerRequest)
    {
        CustomerEntity customerEntity =new CustomerEntity();
        System.out.println(signUpCustomerRequest.getFirstName()+" "+signUpCustomerRequest.getLastName());
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstname(signUpCustomerRequest.getFirstName());
        customerEntity.setPassword(signUpCustomerRequest.getPassword());
        customerEntity.setContactnumber(signUpCustomerRequest.getContactNumber());
        customerEntity.setEmail(signUpCustomerRequest.getEmailAddress());
        customerEntity.setLastname(signUpCustomerRequest.getLastName());
        customerEntity.setSalt("1234abc");
        CustomerEntity createdCustomerEntity= customerService.createCustomer(customerEntity);
        SignupCustomerResponse signupUserResponse=new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(signupUserResponse, HttpStatus.CREATED);
    }


}
