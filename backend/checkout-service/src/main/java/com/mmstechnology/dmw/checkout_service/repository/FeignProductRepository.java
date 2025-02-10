package com.mmstechnology.dmw.checkout_service.repository;

import com.mmstechnology.dmw.checkout_service.model.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient(name = "product-service", configuration = FeignConfig.class)
////@FeignClient(name = "product", url = "http://localhost:8080")
////@LoadBalancerClient(value = "product-service", configuration = LoadBalancerConfiguration.class)
//public interface FeignProductRepository {
//
//    @RequestMapping(method = RequestMethod.GET, value = "/products")
//    @CircuitBreaker(name = "product", fallbackMethod = "getProductByIdFallbackMethod")
//    ProductDto getProductById(@RequestParam String id, @RequestParam Boolean throwError);
//}
@FeignClient(name = "product-service")
public interface FeignProductRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/products")
    ResponseEntity<ProductDto> getProductById(@RequestParam String id, @RequestParam Boolean throwError);
}