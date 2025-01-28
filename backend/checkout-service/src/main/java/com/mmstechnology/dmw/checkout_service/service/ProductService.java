package com.mmstechnology.dmw.checkout_service.service;

import com.mmstechnology.dmw.checkout_service.model.dto.ProductDto;

public interface ProductService {

    ProductDto getProductById(String id);
}
