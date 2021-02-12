package com.retail.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPrice {
    @JsonProperty("currency_code")
    private String currencyCode;
    private  Double value;
}
