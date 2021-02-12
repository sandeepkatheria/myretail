package com.retail.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ProductDtlResponse is response dto
 */
public class ProductDtlResponse {

    private Long id;
    private String name;
    private ProductPrice currentPrice;

    public ProductDtlResponse(Long id, String name, @JsonProperty("current_price") ProductPrice currentPrice) {
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductPrice getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(ProductPrice currentPrice) {
        this.currentPrice = currentPrice;
    }
}
