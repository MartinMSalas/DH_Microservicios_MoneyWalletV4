package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.dto.CheckoutDto;
import com.mmstechnology.dmw.wallet_service.model.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final ProductService productService;

    public CheckoutServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public CheckoutDto buildCheckout(List<String> productsIds) {



        Double total = 0.0;
        for(String id : productsIds) {
            ProductDto product = productService.getProductById(id);
            System.out.println("Response from product-service instance: " + product.getInstance());
            total += product.getPrice();
        }
        CheckoutDto checkoutDto = new CheckoutDto("234","www.digitalhouse.com/checkout?code=234",total.toString(),List.of("credit card","paypal"));
        return checkoutDto;
    }
}
