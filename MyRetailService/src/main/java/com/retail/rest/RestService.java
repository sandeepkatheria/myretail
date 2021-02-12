package com.retail.rest;

import reactor.core.publisher.Mono;

public interface RestService {
    Mono<String> getProductName(Long id);
}
