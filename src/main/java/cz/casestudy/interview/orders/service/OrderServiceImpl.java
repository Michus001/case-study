package cz.casestudy.interview.orders.service;

import cz.casestudy.interview.orders.model.Order;
import cz.casestudy.interview.orders.model.OrderStatus;
import cz.casestudy.interview.orders.repository.OrderRepository;
import cz.casestudy.interview.products.api.BookingStatus;
import cz.casestudy.interview.products.api.ProductBooking;
import cz.casestudy.interview.products.api.ProductPublicService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    @NonNull
    private final ProductPublicService productPublicService;

    @NonNull
    private final OrderRepository orderRepository;

    @Transactional
    public void cancelOrder(UUID orderId) {
        Assert.notNull(orderId, "orderId must not be null");

        orderRepository.findById(orderId).ifPresent(order -> {
            productPublicService.cancelBookings(order.getBookingIds(), BookingStatus.CANCELLED);
        });
        orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);
    }

    public void payOrder(final UUID orderId) {
        Assert.notNull(orderId, "orderId must not be null");

        orderRepository.findById(orderId).ifPresent(order -> {
            productPublicService.confirmBookings(order.getBookingIds());
        });
        orderRepository.updateStatus(orderId, OrderStatus.PAID);
    }

    @Transactional
    public Order createOrder(Collection<ProductBooking> productBookings) {
        Assert.notNull(productBookings, "productBookings must not be null");

        final Set<UUID> bookingIds = productPublicService.bookProduct(productBookings);
        return orderRepository.saveAndFlush(Order.builder().orderStatus(OrderStatus.NEW).bookingIds(bookingIds).build());
    }
}
