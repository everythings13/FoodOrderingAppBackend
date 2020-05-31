package com.upgrad.FoodOrderingApp.api;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionManager {

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ErrorResponse> categoryNotFoundException(CategoryNotFoundException cne) {
    return new ResponseEntity<>(
        new ErrorResponse().code(cne.getCode()).message(cne.getErrorMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CouponNotFoundException.class)
  public ResponseEntity<ErrorResponse> couponNotFoundException(CouponNotFoundException cpne) {
    return new ResponseEntity<>(
        new ErrorResponse().code(cpne.getCode()).message(cpne.getErrorMessage()),
        HttpStatus.NOT_FOUND);
  }
}
