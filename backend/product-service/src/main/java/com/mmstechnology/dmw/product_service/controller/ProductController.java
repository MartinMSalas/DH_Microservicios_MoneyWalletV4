package com.mmstechnology.dmw.product_service.controller;

import com.mmstechnology.dmw.product_service.model.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @GetMapping
    public ProductDto getProduct(@RequestParam String id, @RequestParam(defaultValue = "false") Boolean throwError) {
        log.info("In product service, getProduct with id: "+id);
        if(throwError)
            throw new RuntimeException("Error - boolean activated for product service");
        return new ProductDto(id, "Product - notebook", 2000.0,"1");
    }
}
