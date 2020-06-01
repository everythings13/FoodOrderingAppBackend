package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/payment")
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @RequestMapping(
      method = RequestMethod.GET,
      path = "/payment",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<PaymentListResponse> getAllPaymentMethods() {
    List<PaymentResponse> paymentReponseList =
        paymentService.getAllPaymentMethods().stream()
            .map(RestaurantControllerReponseUtil::getPaymentListObject)
            .collect(Collectors.toList());
    return new ResponseEntity<>(
        new PaymentListResponse().paymentMethods(paymentReponseList), HttpStatus.OK);
  }
}
