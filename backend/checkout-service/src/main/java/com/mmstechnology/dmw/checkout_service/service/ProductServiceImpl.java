package com.mmstechnology.dmw.checkout_service.service;

import com.mmstechnology.dmw.checkout_service.model.dto.ProductDto;
import com.mmstechnology.dmw.checkout_service.repository.FeignProductRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final FeignProductRepository feignProductRepository;


    Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(FeignProductRepository feignProductRepository) {
        this.feignProductRepository = feignProductRepository;

    }

//    @Override
//    @CircuitBreaker(name="product",fallbackMethod = "getProductByIdFallbackMethod")
//    @Retry(name="product")
//    public ProductDto getProductById(String id) {
//        log.info("obtaining product info with ID: " + id);
//        return feignProductRepository.getProductById(id,true);
//    }

    @Override
    @CircuitBreaker(name="product", fallbackMethod = "getProductByIdFallbackMethod")
    @Retry(name="product")
    public ProductDto getProductById(String id) {
        log.info("🔄 Attempting to fetch product with ID: " + id);
       // log.info("🔍 Circuit Breaker State Before Call: " + circuitBreaker.getState());

        try {
            ResponseEntity<ProductDto> response = feignProductRepository.getProductById(id, false);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("❌ Feign call failed with status: " + response.getStatusCode());
                throw new RuntimeException("Feign client returned non-200 response.");
            }

            log.info("✅ Response received: " + response.getBody());
            return response.getBody();

        } catch (Exception e) {
            log.error("❌ Exception thrown: " + e.getMessage());
            throw e; // Ensure the exception is propagated to trigger the Circuit Breaker
        }
    }
    public ProductDto getProductByIdFallbackMethod(String id, Throwable throwable){
        log.error("Circuit Breaker OPEN !!! - Reason: " + throwable.getMessage());
       // Log circuit breaker state
        return new ProductDto();
    }
}
