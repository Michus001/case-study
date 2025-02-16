package cz.casestudy.interview.products.api;

import java.util.UUID;

/**
 * Represents a product that is missing in the stock.
 */
public record MissingProduct(UUID productId, Integer missingQuantity) {
}
