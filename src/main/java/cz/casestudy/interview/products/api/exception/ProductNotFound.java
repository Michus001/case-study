package cz.casestudy.interview.products.api.exception;

import lombok.Getter;

import java.util.UUID;

/**
 * Exception thrown when product is not found.
 */
@Getter
public class ProductNotFound extends RuntimeException {

  private final UUID productId;

  public ProductNotFound(String message, UUID productId) {
        super(message);
      this.productId = productId;
  }
}
