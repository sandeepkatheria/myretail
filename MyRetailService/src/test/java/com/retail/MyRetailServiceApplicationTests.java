package com.retail;

import com.retail.controller.ProductDetailsController;
import com.retail.domain.Product;
import com.retail.model.ProductDtlResponse;
import com.retail.model.ProductPrice;
import com.retail.repository.ProductRepository;
import com.retail.service.ProductDetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProductDetailsController.class)
class MyRetailServiceApplicationTests {
	@MockBean
	ProductRepository productRepository;
	@MockBean
	ProductDetailService productDetailService;
	@Autowired
	private WebTestClient webClient;

	@Test
	void testCreateEmployee() {
		Product product = new Product();
		product.setId(1001l);
		product.setName("TestProduct");
		product.setPrice(220.23);
		product.setCurrencyType("USD");

		ProductPrice productPrice = new ProductPrice("USD",220.23);
		ProductDtlResponse responsedto = new ProductDtlResponse(1001l,"TestProduct",productPrice);

		Mockito.when(productRepository.findById(1001l))
				.thenReturn(Mono.just(product));

		Mockito.when(productDetailService.getProductDetails(1001l))
				.thenReturn(Mono.just(responsedto));


		webClient.get().uri("/product/details/{id}", 1001l)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(1001);


	}

}
