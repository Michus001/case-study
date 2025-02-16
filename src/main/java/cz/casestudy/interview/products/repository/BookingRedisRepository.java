package cz.casestudy.interview.products.repository;

import cz.casestudy.interview.products.model.BookingExpirationNotification;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Repository for booking expiration notifications
 */
public interface BookingRedisRepository extends CrudRepository<BookingExpirationNotification, UUID> {
}
