package com.mmstechnology.dmw.product_service.controller;

import com.mmstechnology.dmw.product_service.model.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @GetMapping
    public ProductDto getProduct(@RequestParam String id) {
        return new ProductDto(id, "Product - notebook", 2000.0,"1");
    }
}
