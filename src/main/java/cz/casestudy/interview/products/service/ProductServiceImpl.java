package cz.casestudy.interview.products.service;

import cz.casestudy.interview.products.api.exception.ProductNotFound;
import cz.casestudy.interview.products.model.Product;
import cz.casestudy.interview.products.model.ProductStatus;
import cz.casestudy.interview.products.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    @NonNull
    private final ProductRepository productRepository;

    public void save(Product product) {
        Assert.notNull(product, "product must not be null");
        productRepository.saveAndFlush(product);
    }

    public void delete(UUID id) {
        Assert.notNull(id, "id must not be null");

        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            final Product productEntity = product.get();
            productEntity.setStatus(ProductStatus.DELETED);
            productRepository.saveAndFlush(productEntity);
            return;
        }
        throw new ProductNotFound("Product not found", id);
    }

    public void modify(UUID productId, Product product) {
        Assert.notNull(productId, "productId must not be null");
        Assert.notNull(product, "product must not be null");

        Optional<Product> productEntity = productRepository.findById(productId);
        if (productEntity.isPresent()) {
            Product productToUpdate = productEntity.get();
            productToUpdate.setName(product.getName());
            productToUpdate.setPrice(product.getPrice());
            productToUpdate.setQuantity(product.getQuantity());
            productToUpdate.setUnit(product.getUnit());
            productRepository.saveAndFlush(productToUpdate);
            return;
        }
        throw new ProductNotFound("Product not found", productId);
    }
}
