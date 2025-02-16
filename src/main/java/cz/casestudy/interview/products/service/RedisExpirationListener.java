package cz.casestudy.interview.products.service;

import cz.casestudy.interview.products.api.BookingStatus;
import cz.casestudy.interview.products.api.ProductPublicService;
import cz.casestudy.interview.products.model.BookingExpirationNotification;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Listener for Redis expiration events.
 */
@Slf4j
@AllArgsConstructor
@Component
public class RedisExpirationListener {

    @NonNull
    private final ProductPublicService productPublicService;

    @EventListener
    public void onMessage(final RedisKeyExpiredEvent<BookingExpirationNotification> event) {
        log.info("Received message with expiration: {}", event);
        productPublicService.cancelBookings(Set.of(((BookingExpirationNotification)event.getValue()).getBookingId()), BookingStatus.EXPIRED);
    }
}
