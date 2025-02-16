package cz.casestudy.interview.products.controller;

import cz.casestudy.interview.TestRedisConfiguration;

import cz.casestudy.interview.product.rest.model.Product;
import cz.casestudy.interview.products.model.ProductStatus;
import cz.casestudy.interview.products.repository.BookingRepository;
import cz.casestudy.interview.products.repository.ProductRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestRedisConfiguration.class)
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
public class ProductControllerTest {

    @Inject
    private ProductController productController;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        productRepository.deleteAll();
        productRepository.flush();
    }

    @Test
    void createProduct() {
        productController.createProduct(new Product().name("Houska").price(BigDecimal.valueOf(10.1)).quantity(2).unit("ks"));
        final List<cz.casestudy.interview.products.model.Product> product = productRepository.findAll();
        assertThat(product).hasSize(1);
        assertThat(product.get(0).getName()).isEqualTo("Houska");
        assertThat(product.get(0).getPrice().doubleValue()).isEqualTo(10.1);
        assertThat(product.get(0).getQuantity()).isEqualTo(2);
        assertThat(product.get(0).getUnit()).isEqualTo("ks");
        assertThat(product.get(0).getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    @Transactional
    void deleteProduct() {
        productController.createProduct(new Product().name("Houska").price(BigDecimal.valueOf(10.1)).quantity(2).unit("ks"));
        final List<cz.casestudy.interview.products.model.Product> product = productRepository.findAll();
        productController.deleteProduct(product.get(0).getId());

        final cz.casestudy.interview.products.model.Product productResult = productRepository.getReferenceById(product.get(0).getId());
        assertThat(productResult.getName()).isEqualTo("Houska");
        assertThat(productResult.getPrice().doubleValue()).isEqualTo(10.1);
        assertThat(productResult.getQuantity()).isEqualTo(2);
        assertThat(productResult.getUnit()).isEqualTo("ks");
        assertThat(productResult.getStatus()).isEqualTo(ProductStatus.DELETED);
    }

    @Test
    @Transactional
    void modifyProduct() {
        productController.createProduct(new Product().name("Houska").price(BigDecimal.valueOf(10.1)).quantity(2).unit("ks"));
        final List<cz.casestudy.interview.products.model.Product> product = productRepository.findAll();
        productController.modifyProduct(product.get(0).getId(), new Product().name("Rohlik").unit("ks").price(BigDecimal.valueOf(11)).quantity(7));

        final cz.casestudy.interview.products.model.Product productResult = productRepository.getReferenceById(product.get(0).getId());
        assertThat(productResult.getName()).isEqualTo("Rohlik");
        assertThat(productResult.getPrice().doubleValue()).isEqualTo(11);
        assertThat(productResult.getQuantity()).isEqualTo(7);
        assertThat(productResult.getUnit()).isEqualTo("ks");
        assertThat(productResult.getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }
}
