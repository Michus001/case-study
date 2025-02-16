package cz.casestudy.interview.products.service;

import cz.casestudy.interview.products.model.Product;

import java.util.UUID;

/**
 * Service interface for managing products.
 */
public interface ProductService {

    /**
     * Saves a product.
     *
     * @param product the product to save, must not be null
     */
    void save(Product product);

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete, must not be null
     */
    void delete(UUID id);

    /**
     * Modifies an existing product.
     *
     * @param productId the ID of the product to modify, must not be null
     * @param product the product details to update, must not be null
     */
    void modify(UUID productId, Product product);
}
