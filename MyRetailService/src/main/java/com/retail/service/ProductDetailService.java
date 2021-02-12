package com.retail.service;

import com.retail.model.ProductDtlResponse;
import com.retail.model.ProductPrice;
import reactor.core.publisher.Mono;


public interface ProductDetailService {
    Mono<ProductDtlResponse> getProductDetails(Long pid);
    Mono<ProductDtlResponse> update(Long pid, ProductPrice price);
}
