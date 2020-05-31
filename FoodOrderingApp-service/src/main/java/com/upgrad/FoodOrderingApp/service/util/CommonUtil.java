package com.upgrad.FoodOrderingApp.service.util;


import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

import java.util.Base64;
import java.util.regex.Pattern;

public class CommonUtil {

    public static boolean tokenHeaderCheck(String authorization)
    {
        boolean validHeader = true;
        if(authorization == null || authorization.isEmpty())
        {
            validHeader = false;
        }
        else {
            if (!authorization.contains("Bearer")) {
                validHeader = false;
            }
            String token = authorization.split("Bearer ")[1];

            if (token == null || token.isEmpty()) {
                validHeader = false;
            }
        }
        return validHeader;
    }

    public static boolean basicAuthHeaderCheck(String authorization)
    {
        boolean validHeader = true;
        if(!authorization.contains("Basic"))
        {
            validHeader = false;
        }
        else {
            String[] credentials = new String(Base64.getDecoder().decode(authorization.split("Basic ")[1])).split(":");
            if (credentials.length == 0) {
                validHeader = false;
            }
        }
        return validHeader;
    }

    public static boolean isNullOrEmpty(String value)
    {
        boolean isNullOrEmpty = false;
        if(value == null || value.isEmpty())
        {
            isNullOrEmpty = true;
        }
        return isNullOrEmpty;
    }

    public static boolean customerEntityCheck(CustomerEntity customerEntity)
    {
        boolean notValid = false;
        if(isNullOrEmpty(customerEntity.getFirstname()) || isNullOrEmpty(customerEntity.getPassword())|| isNullOrEmpty(customerEntity.getEmail())||isNullOrEmpty(customerEntity.getContactnumber()))
        {
            notValid = true;
        }
        return notValid;
    }

    public static boolean emailValidation(String email)
    {
        return Pattern.matches("(^[A-Za-z0-9+_.-]+@(.+)$)",email);
    }

    public static boolean contactNumberCheck(String contactNumber)
    {
        boolean isValid = true;
        if(contactNumber.length() == 10)
        {
            if(!Pattern.matches("(^[0-9]*$)",contactNumber))
            {
                isValid = false;
            }
        }
        else
            {
                isValid = false;
            }

        return isValid;
    }

    public static boolean passwordCheck (String password)
    {
        return Pattern.matches("(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[[#@$%&*!^]])(?=\\S+$).{8,}$)",password);
    }
}
