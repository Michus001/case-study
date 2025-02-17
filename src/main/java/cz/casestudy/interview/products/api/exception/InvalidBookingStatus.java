package cz.casestudy.interview.products.api.exception;

/**
 * Exception for invalid booking status.
 */
public class InvalidBookingStatus extends RuntimeException {
    public InvalidBookingStatus(final String message) {
        super(message);
    }
}
