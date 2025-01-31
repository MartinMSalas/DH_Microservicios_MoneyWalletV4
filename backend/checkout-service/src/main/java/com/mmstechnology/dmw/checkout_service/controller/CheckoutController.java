package com.mmstechnology.dmw.checkout_service.controller;

import com.mmstechnology.dmw.checkout_service.model.dto.CheckoutDto;
import com.mmstechnology.dmw.checkout_service.service.CheckoutService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping
    public CheckoutDto getCheckout(@RequestParam List<String> productsIds, @RequestHeader("X-Request-from") String requestFrom) {
        System.out.println("Request from: " + requestFrom);
        if(!requestFrom.equals("Gateway")) {
            throw new RuntimeException("Unauthorized access");
        }
        return checkoutService.buildCheckout(productsIds);
    }
}
