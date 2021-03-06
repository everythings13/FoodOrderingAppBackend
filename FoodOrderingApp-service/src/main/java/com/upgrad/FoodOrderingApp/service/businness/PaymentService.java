package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.exception.PaymentMethodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

  @Autowired private PaymentDao paymentDao;

  /** @return list of all payment methods */
  public List<PaymentEntity> getAllPaymentMethods() {
    return paymentDao.getAllPaymentMethods();
  }

  public PaymentEntity getPaymentByUUID(String uuid) throws PaymentMethodNotFoundException {
    PaymentEntity payment = paymentDao.getPaymentByUUId(uuid);
    if (payment == null) {
      throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
    }
    return payment;
  }

  public PaymentEntity getPaymentById(Integer id) throws PaymentMethodNotFoundException {
    PaymentEntity payment = paymentDao.getPaymentById(id);
    if (payment == null) {
      throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
    }
    return payment;
  }
}
