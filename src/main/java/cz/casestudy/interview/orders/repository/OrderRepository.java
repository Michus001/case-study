package cz.casestudy.interview.orders.repository;

import cz.casestudy.interview.orders.model.Order;
import cz.casestudy.interview.orders.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * Repository for Order entity
 */
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Update order status.
     *
     * @param orderId order id
     * @param orderStatus new order status
     */
    @Modifying
    @Query("update Order o set o.orderStatus = :orderStatus where o.orderId = :orderId")
    void updateStatus(UUID orderId, OrderStatus orderStatus);
}
