package cz.casestudy.interview.products.controller;


import cz.casestudy.interview.product.rest.api.ProductApi;
import cz.casestudy.interview.product.rest.model.Product;
import cz.casestudy.interview.products.service.ProductServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller for managing products.
 */
@AllArgsConstructor
@RestController
public class ProductController implements ProductApi {

    @NonNull
    private final ProductServiceImpl productService;

    @NonNull
    private final ProductMapper productMapper;

    @Override
    public ResponseEntity<Void> createProduct(Product product) {
        productService.save(productMapper.toProduct(product));

        return ResponseEntity.ok().build();
    }

    @Transactional
    @Override
    public ResponseEntity<Void> deleteProduct(UUID productId) {
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<Void> modifyProduct(UUID productId, Product product) {

        productService.modify(productId, productMapper.toProduct(product));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
