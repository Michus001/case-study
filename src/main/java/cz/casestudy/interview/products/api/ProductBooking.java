package cz.casestudy.interview.products.api;

import java.util.UUID;

public record ProductBooking(UUID productId, Integer quantity, String unit) {
}
