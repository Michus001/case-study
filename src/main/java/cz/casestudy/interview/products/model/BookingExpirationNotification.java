package cz.casestudy.interview.products.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

/**
 * Represents a booking expiration notification.
 */
@Getter
@RedisHash
@AllArgsConstructor
public class BookingExpirationNotification {
    @Id
    private UUID bookingId;
}
