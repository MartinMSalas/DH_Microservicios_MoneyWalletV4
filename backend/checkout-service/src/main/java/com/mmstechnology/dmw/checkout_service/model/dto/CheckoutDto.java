package com.mmstechnology.dmw.checkout_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDto {
    private String id;
    private String url;
    private String totalAmount;

    private List<String> availableMethods;
}
