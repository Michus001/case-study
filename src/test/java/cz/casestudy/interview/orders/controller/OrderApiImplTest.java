package cz.casestudy.interview.orders.controller;

import cz.casestudy.interview.TestRedisConfiguration;
import cz.casestudy.interview.order.rest.model.Order;
import cz.casestudy.interview.order.rest.model.OrderWithId;
import cz.casestudy.interview.order.rest.model.OrderedProduct;
import cz.casestudy.interview.orders.repository.OrderRepository;
import cz.casestudy.interview.products.api.MissingProduct;
import cz.casestudy.interview.products.api.exception.InvalidUnit;
import cz.casestudy.interview.products.api.exception.MissingProductException;
import cz.casestudy.interview.products.api.exception.ProductNotFound;
import cz.casestudy.interview.products.model.Product;
import cz.casestudy.interview.products.model.ProductStatus;
import cz.casestudy.interview.products.repository.ProductRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

/**
 * Tests for {@link OrderApiImpl}.
 */
@SpringBootTest(classes = TestRedisConfiguration.class)
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
class OrderApiImplTest {

    @Inject
    private OrderApiImpl orderApi;

    @Inject
    private ProductRepository productRepository;

    @Test
    void createOrder_validOrder() {
        final Product product = productRepository.saveAndFlush(Product.builder().name("Houska").price(BigDecimal.valueOf(7.3)).quantity(10).unit("ks").status(ProductStatus.ACTIVE).build());
        final ResponseEntity<OrderWithId> response = orderApi.createOrder(new Order().products(List.of(new OrderedProduct().productId(product.getId()).quantity(2).unit("ks"))));
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getProducts()).hasSize(1);

        productRepository.findById(product.getId()).ifPresent(p -> assertThat(p.getQuantity()).isEqualTo(8));
    }

    @Test
    void createOrder_invalidUnit() {
        final Product product = productRepository.saveAndFlush(Product.builder().name("Houska").price(BigDecimal.valueOf(7.3)).quantity(10).unit("ks").status(ProductStatus.ACTIVE).build());
        assertThatThrownBy(() -> orderApi.createOrder(new Order().products(List.of(new OrderedProduct().productId(product.getId()).quantity(2).unit("l"))))).isInstanceOf(InvalidUnit.class);
    }

    @Test
    void createOrder_invalidProduct() {
        assertThatThrownBy(() -> orderApi.createOrder(new Order().products(List.of(new OrderedProduct().productId(UUID.randomUUID()).quantity(2).unit("l"))))).isInstanceOf(ProductNotFound.class);
    }

    @Test
    void createOrder_missingProduct() {
        final Product product = productRepository.saveAndFlush(Product.builder().name("Houska").price(BigDecimal.valueOf(7.3)).quantity(10).unit("ks").status(ProductStatus.ACTIVE).build());
        final MissingProductException exception = catchThrowableOfType(MissingProductException.class, () -> orderApi.createOrder(new Order().products(List.of(new OrderedProduct().productId(product.getId()).quantity(12).unit("ks")))));
        assertThat(exception).isNotNull();
        assertThat(exception.getMissingProducts()).containsExactly(new MissingProduct(product.getId(), 2));
    }


    @Test
    @Transactional
    void cancelOrder() {
        final Product product = productRepository.saveAndFlush(Product.builder().name("Houska").price(BigDecimal.valueOf(7.3)).quantity(10).unit("ks").status(ProductStatus.ACTIVE).build());
        final ResponseEntity<OrderWithId> response = orderApi.createOrder(new Order().products(List.of(new OrderedProduct().productId(product.getId()).quantity(2).unit("ks"))));

        orderApi.cancelOrder(response.getBody().getId());
        productRepository.findById(product.getId()).ifPresent(p -> assertThat(p.getQuantity()).isEqualTo(10));
    }

    @Test
    @Transactional
    void payOrder() {
        final Product product = productRepository.saveAndFlush(Product.builder().name("Houska").price(BigDecimal.valueOf(7.3)).quantity(10).unit("ks").status(ProductStatus.ACTIVE).build());
        final ResponseEntity<OrderWithId> response = orderApi.createOrder(new Order().products(List.of(new OrderedProduct().productId(product.getId()).quantity(2).unit("ks"))));
        productRepository.findById(product.getId()).ifPresent(p -> assertThat(p.getQuantity()).isEqualTo(8));
        orderApi.payOrder(response.getBody().getId());
    }


}