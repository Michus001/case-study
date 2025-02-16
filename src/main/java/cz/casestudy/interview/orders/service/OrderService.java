package cz.casestudy.interview.orders.service;

import cz.casestudy.interview.orders.model.Order;
import cz.casestudy.interview.products.api.ProductBooking;

import java.util.Collection;
import java.util.UUID;


/**
 * Service interface for managing orders.
 */
public interface OrderService {

    /**
     * Cancels an order by its ID.
     *
     * @param orderId the ID of the order to cancel, must not be null
     */
    void cancelOrder(UUID orderId);

    /**
     * Marks an order as paid by its ID.
     *
     * @param orderId the ID of the order to mark as paid, must not be null
     */
    void payOrder(final UUID orderId);

    /**
     * Creates a new order with the given product bookings.
     *
     * @param productBookings the product bookings to include in the order, must not be null
     * @return the created order
     */
    Order createOrder(Collection<ProductBooking> productBookings);
}