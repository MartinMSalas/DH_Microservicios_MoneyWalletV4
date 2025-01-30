package com.mmstechnology.dmw.checkout_service.repository;

import com.mmstechnology.dmw.checkout_service.configuration.LoadBalancerConfiguration;
import com.mmstechnology.dmw.checkout_service.model.dto.ProductDto;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
//@FeignClient(name = "product", url = "http://localhost:8080")
//@LoadBalancerClient(value = "product-service", configuration = LoadBalancerConfiguration.class)
public interface FeignProductRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/products")
    ProductDto getProductById(@RequestParam String id);
}
