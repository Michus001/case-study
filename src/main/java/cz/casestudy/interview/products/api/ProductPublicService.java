package cz.casestudy.interview.products.api;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * Public service interface for managing product bookings.
 */
public interface ProductPublicService {

    /**
     * Books a collection of products.
     *
     * @param productBooking the collection of product bookings to be made, must not be null
     * @return a set of UUIDs representing the booking IDs
     */
    Set<UUID> bookProduct(Collection<ProductBooking> productBooking);

    /**
     * Cancels a collection of bookings.
     *
     * @param bookingIds the collection of booking IDs to be cancelled, must not be null
     * @param status the status to set for the cancelled bookings, must not be null
     */
    void cancelBookings(Collection<UUID> bookingIds, BookingStatus status);

    /**
     * Confirms a collection of bookings.
     *
     * @param bookingIds the collection of booking IDs to be confirmed, must not be null
     */
    void confirmBookings(Collection<UUID> bookingIds);
}
