package com.mmstechnology.dmw.checkout_service.controller;

import com.mmstechnology.dmw.checkout_service.model.dto.CheckoutDto;
import com.mmstechnology.dmw.checkout_service.service.CheckoutService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping
    public CheckoutDto getCheckout(@RequestParam List<String> productsIds) {
        return checkoutService.buildCheckout(productsIds);
    }
}
