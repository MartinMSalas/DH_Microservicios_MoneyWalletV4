package com.mmstechnology.dmw.checkout_service.service;

import com.mmstechnology.dmw.checkout_service.model.dto.CheckoutDto;

import java.util.List;

public interface CheckoutService {
    CheckoutDto buildCheckout(List<String> productsIds);
}
