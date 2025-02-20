package com.mmstechnology.dmw.wallet_service.controller;

import com.mmstechnology.dmw.wallet_service.model.dto.CheckoutDto;
import com.mmstechnology.dmw.wallet_service.service.CheckoutService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }


    @GetMapping("/{id}")
    public CheckoutDto getById(@PathVariable String id){
        return new CheckoutDto(id,"asd","asd", List.of());
    }

    @GetMapping
    public CheckoutDto getCheckout(@RequestParam List<String> productsIds, @RequestHeader("X-Request-from") String requestFrom, @RequestHeader() Map<String, String> headers) {
        System.out.println("Request from: " + requestFrom);
        if(!requestFrom.equals("Gateway")) {
            throw new RuntimeException("Unauthorized access");
        }
        return checkoutService.buildCheckout(productsIds);
    }
}
