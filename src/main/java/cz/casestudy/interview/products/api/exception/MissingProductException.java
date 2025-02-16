package cz.casestudy.interview.products.api.exception;

import cz.casestudy.interview.products.api.MissingProduct;
import lombok.Getter;

import java.util.Set;

/**
 * Exception thrown when customer orders more products than available.
 */
@Getter
public class MissingProductException extends RuntimeException {

  private final Set<MissingProduct> missingProducts;

  /**
   * Constructor.
   *
   * @param message         exception message
   * @param missingProducts set of missing products
   */
  public MissingProductException(String message, Set<MissingProduct> missingProducts) {
        super(message);
        this.missingProducts = missingProducts;
    }
}
