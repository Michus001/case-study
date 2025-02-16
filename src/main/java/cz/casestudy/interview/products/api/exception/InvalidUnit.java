package cz.casestudy.interview.products.api.exception;

/**
 * Exception thrown when the unit is invalid.
 */
public class InvalidUnit extends RuntimeException {
    public InvalidUnit(String message) {
        super(message);
    }
}
