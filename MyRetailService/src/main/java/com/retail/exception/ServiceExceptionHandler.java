package com.retail.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

/**
 * ServiceExceptionHandler class works as global Exceptional Handler
 */
@Slf4j
@Component
@Order(-2)
public class ServiceExceptionHandler extends AbstractErrorWebExceptionHandler {

    /**
     *
     * @param errorAttributes
     * @param resources
     * @param applicationContext
     * @param codecConfigurer
     */
    public ServiceExceptionHandler(ErrorAttributes errorAttributes,WebProperties.Resources resources,
                                   ApplicationContext applicationContext, ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
        this.setMessageReaders(codecConfigurer.getReaders());
    }

    /**
     * RouterFunction is mapped for all mapping and generic handlerfunction
     * @param errorAttributes
     * @return
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(),this::errorHandler);
    }

    /**
     * errorHandler method works as error routinghandle, It handles all exception globally
     * @param request
     * @return
     */
    private Mono<ServerResponse> errorHandler(ServerRequest request){
        Map<String,Object> errorAttributes =  getErrorAttributes(request,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        int status = (int)Optional.ofNullable(errorAttributes.get("status")).orElse(500);
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorAttributes));
    }
}
