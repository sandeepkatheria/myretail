package com.retail.service.impl;

import com.retail.domain.Product;
import com.retail.exception.ProductDetailsException;
import com.retail.model.ProductDtlResponse;
import com.retail.model.ProductPrice;
import com.retail.repository.ProductRepository;
import com.retail.rest.RestService;
import com.retail.service.ProductDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * ProductDetailServiceImpl
 */
@Slf4j
@Service
public class ProductDetailServiceImpl implements ProductDetailService {

    @Autowired
    RestService restService;
    @Autowired
    ProductRepository productRepository;

    /**
     * getProductDetails is used to get data from db and enriches price details from api
     * @param pid
     * @return
     */
    @Override
    public Mono<ProductDtlResponse> getProductDetails(Long pid){
          return productRepository.findById(pid)
                  .flatMap(product -> restService.getProductName(product.getId())
                          .map(name -> getProductDtlResponse(product,name))
                  );
    }

    /**
     * update method is used to update existing product price details
     * @param pid
     * @param price
     * @return
     */
    public Mono<ProductDtlResponse> update(Long pid, ProductPrice price) {
        Mono<Product> product =  productRepository.findById(pid)
                .switchIfEmpty(Mono.error(new ProductDetailsException("Product id "+pid+" is not found")));
        if(price.getValue() !=null && !price.getCurrencyCode().trim().isEmpty()){
            Product product1 = product.block();
            product1.setCurrencyType(price.getCurrencyCode());
            product1.setPrice(price.getValue());
            productRepository.save(product1).onErrorResume( e -> Mono.error(new ProductDetailsException(e.getMessage(),e)));
            return Mono.just(new ProductDtlResponse(product1.getId(),product1.getName(),price));
        }
        return Mono.empty();
    }

    /**
     * getProductDtlResponse is helper method in order to instantiate ProductDtlResponse
     * @param product
     * @param productName
     * @return
     */
    private ProductDtlResponse getProductDtlResponse(Product product,String productName){
        ProductPrice productPrice = new ProductPrice(product.getCurrencyType(),product.getPrice());
       return new ProductDtlResponse(product.getId(),productName,productPrice);
    }

}
