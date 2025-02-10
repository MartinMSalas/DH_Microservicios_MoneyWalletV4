package com.mmstechnology.dmw.checkout_service.service;

import com.mmstechnology.dmw.checkout_service.model.dto.ProductDto;
import org.springframework.web.bind.annotation.RequestParam;

public interface ProductService {

    ProductDto getProductById(@RequestParam String id);
}
