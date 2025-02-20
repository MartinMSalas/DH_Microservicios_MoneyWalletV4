package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.dto.CheckoutDto;

import java.util.List;

public interface CheckoutService {
    CheckoutDto buildCheckout(List<String> productsIds);
}
