package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(RestaurantNotFoundException.class)
  public ResponseEntity<ErrorResponse> RestaurantNotFoundException(
          RestaurantNotFoundException exc) {
    return new ResponseEntity<>(
            new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
            HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ErrorResponse> CategoryNotFoundException(CategoryNotFoundException exc) {
    return new ResponseEntity<>(
        new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidRatingException.class)
  public ResponseEntity<ErrorResponse> InvalidRatingException(
          InvalidRatingException exc) {
    return new ResponseEntity<>(
            new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
            HttpStatus.FORBIDDEN);
  }
}
