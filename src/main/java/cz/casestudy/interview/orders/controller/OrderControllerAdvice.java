package cz.casestudy.interview.orders.controller;

import cz.casestudy.interview.order.rest.model.Error;
import cz.casestudy.interview.products.api.MissingProduct;
import cz.casestudy.interview.products.api.exception.InvalidUnit;
import cz.casestudy.interview.products.api.exception.MissingProductException;
import cz.casestudy.interview.products.api.exception.ProductNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller advice for handling exceptions in the order controller.
 */
@ControllerAdvice
public class OrderControllerAdvice {

    @ExceptionHandler(MissingProductException.class)
    public ResponseEntity<Error> handleMissingProduct(MissingProductException e) {
        Assert.notNull(e, "Exception must not be null");

        return ResponseEntity.badRequest().body(new Error()
                .code("MISSING_PRODUCTS")
                .id(UUID.randomUUID())
                .otherData(e.getMissingProducts().stream().collect(Collectors.toMap(MissingProduct::productId, MissingProduct::missingQuantity))));
    }

    @ExceptionHandler(InvalidUnit.class)
    public ResponseEntity<Error> handleInvalidUnit(InvalidUnit e) {
        return ResponseEntity.badRequest().body(new Error()
                .code("INVALID_PRODUCT_UNIT")
                .id(UUID.randomUUID()));
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<Error> handleMissingProduct(ProductNotFound e) {
        return ResponseEntity.notFound().build();
    }
}
