package cz.casestudy.interview.products.repository;

import cz.casestudy.interview.products.model.Booking;
import cz.casestudy.interview.products.api.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

/**
 * Repository for booking entity.
 */
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Modifying
    @Query("update Booking b set b.expiresAt = null, b.status = :bookingStatus where b.id = :bookingId and b.status = 'NEW'")
    void updateStatus(UUID bookingId, BookingStatus bookingStatus);

}
