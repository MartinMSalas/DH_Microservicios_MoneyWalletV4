package com.mmstechnology.dmw.checkout_service.service;

import com.mmstechnology.dmw.checkout_service.model.dto.ProductDto;
import com.mmstechnology.dmw.checkout_service.repository.FeignProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final FeignProductRepository feignProductRepository;

    public ProductServiceImpl(FeignProductRepository feignProductRepository) {
        this.feignProductRepository = feignProductRepository;
    }

    @Override
    public ProductDto getProductById(String id) {
        return feignProductRepository.getProductById(id);
    }
}
