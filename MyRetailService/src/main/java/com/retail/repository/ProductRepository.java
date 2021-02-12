package com.retail.repository;

import com.retail.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface ProductRepository extends ReactiveMongoRepository<Product,Long> {
}
