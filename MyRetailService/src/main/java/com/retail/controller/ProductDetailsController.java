package com.retail.controller;


import com.retail.model.ProductDtlResponse;
import com.retail.model.ProductPrice;
import com.retail.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * ProductDetailsController
 * url mapping /product/details/{id}
 */
@RestController
@RequestMapping("/product/details/")
public class ProductDetailsController {

    @Autowired
    ProductDetailService productDetailService;

    /**
     * getProductDetails is used to extract data from db.
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Mono<ResponseEntity<ProductDtlResponse>> getProductDetails(@PathVariable Long id){
        return productDetailService.getProductDetails(id)
                .map(data -> ResponseEntity.ok(data))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * update method is used to updated price details of existing product item
     * @param id
     * @param productprice
     * @return
     */
    @PutMapping("{id}")
    public Mono<ResponseEntity<ProductDtlResponse>> update(@PathVariable Long id,
                                                           @RequestBody ProductPrice productprice){
        return productDetailService.update(id,productprice)
                .map(data -> ResponseEntity.ok(data))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
