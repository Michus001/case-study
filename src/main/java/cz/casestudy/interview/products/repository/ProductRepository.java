package cz.casestudy.interview.products.repository;

import cz.casestudy.interview.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Product repository.
 */
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = "update product p set quantity = p.quantity - :quantity where p.product_id = :id and p.quantity - :quantity >= 0 and p.unit = :unit and p.status = 'ACTIVE' returning p.quantity", nativeQuery = true)
    Integer decreaseAmount(@Param("id") UUID id, @Param("quantity") Integer quantity, String unit);


    @Modifying
    @Query(value = "update Product p set p.quantity = p.quantity + :quantity where p.id = :id")
    void increaseAmount(@Param("id") UUID id, @Param("quantity") Integer quantity);

    @Query(value = "select * from product p where p.product_id = :id and p.status = 'ACTIVE'", nativeQuery = true)
    Optional<Product> findById(UUID id);
}
