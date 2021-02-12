package com.retail.rest.impl;

import com.retail.exception.ProductDetailsException;
import com.retail.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class RestServiceImpl implements RestService {

    @Value("${rest_api_host}")
    private String baseUrl;
    @Value("${rest_api_url}")
    private String url;
    private WebClient webClient;
    private final int retry = 3;

    /**
     *
     */
    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest()).filter(logResponse()).build();
    }

    /**
     *
     * @return
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response Status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });

    }

    /**
     *
     * @return
     */
    private ExchangeFilterFunction logRequest() {
       return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {}", clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    /**
     *
     * @param pid
     * @return
     */
    @Override
    public Mono<String> getProductName(Long pid) {
        return webClient.get().uri(url, pid).retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new ProductDetailsException("Service is not found"))
                )
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new ProductDetailsException("Internal Server Error"))
                )
                .bodyToMono(String.class).retry(retry);
    }
}
