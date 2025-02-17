package cz.casestudy.interview.products.service;

import cz.casestudy.interview.configuration.ApplicationProperties;
import cz.casestudy.interview.products.api.BookingStatus;
import cz.casestudy.interview.products.api.MissingProduct;
import cz.casestudy.interview.products.api.ProductBooking;
import cz.casestudy.interview.products.api.ProductPublicService;
import cz.casestudy.interview.products.api.exception.InvalidBookingStatus;
import cz.casestudy.interview.products.api.exception.InvalidUnit;
import cz.casestudy.interview.products.api.exception.MissingProductException;
import cz.casestudy.interview.products.api.exception.ProductNotFound;
import cz.casestudy.interview.products.model.Booking;
import cz.casestudy.interview.products.model.BookingExpirationNotification;
import cz.casestudy.interview.products.model.Product;
import cz.casestudy.interview.products.repository.BookingRedisRepository;
import cz.casestudy.interview.products.repository.BookingRepository;
import cz.casestudy.interview.products.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ProductPublicServiceImpl implements ProductPublicService {

    @NonNull
    private ProductRepository productRepository;

    @NonNull
    private BookingRepository bookingRepository;

    @NonNull
    private BookingRedisRepository bookingRedisRepository;

    @NonNull
    private ApplicationProperties applicationProperties;

    @Override
    public Set<UUID> bookProduct(final Collection<ProductBooking> productBooking) {
        final Set<MissingProduct> missingProducts = new HashSet<>();
        final Set<UUID> bookingIds = new HashSet<>();
        productBooking.forEach(booking -> {
            Integer remaining = productRepository.decreaseAmount(booking.productId(), booking.quantity(), booking.unit());
            Optional<Product> product = productRepository.findById(booking.productId());
            if (remaining == null) {
                missingProducts.add(new MissingProduct(booking.productId(), handleBookingError(booking, product)));
            } else {
                bookingIds.add(handleBookingSuccess(booking, product.get()));
            }
        });
        if (CollectionUtils.isEmpty(missingProducts)) {
            return bookingIds;
        }
        throw new MissingProductException("Some products are missing", missingProducts);
    }

    @Async
    @Transactional
    @Override
    public void cancelBookings(Collection<UUID> bookingIds, BookingStatus bookingStatus) {
        bookingRedisRepository.deleteAllById(bookingIds);
        bookingIds.forEach(bookingId -> {
            Booking booking = bookingRepository.getReferenceById(bookingId);
            productRepository.increaseAmount(booking.getProduct().getId(), booking.getQuantity());
            bookingRepository.updateStatus(bookingId, bookingStatus);
        });
    }

    @Transactional
    @Override
    public void confirmBookings(Collection<UUID> bookingIds) {
        bookingRedisRepository.deleteAllById(bookingIds);
        bookingIds.forEach(bookingId -> {
            if (bookingRepository.updateStatus(bookingId, BookingStatus.CONFIRMED) != 1) {
                throw new InvalidBookingStatus("Invalid booking status");
            };
        });
    }

    private int handleBookingError(ProductBooking productBooking, Optional<Product> product) {
        if (product.isEmpty()) {
            throw new ProductNotFound("Product not found", productBooking.productId());
        }
        Product existingProduct = product.get();
        if (!productBooking.unit().equalsIgnoreCase(existingProduct.getUnit())) {
            throw new InvalidUnit("Invalid unit");
        }
        return productBooking.quantity() - existingProduct.getQuantity();
    }

    private UUID handleBookingSuccess(ProductBooking productBooking, Product product) {
        Booking booking = bookingRepository.save(Booking.builder().status(BookingStatus.NEW).product(product).expiresAt(Instant.now().plusMillis(applicationProperties.getBookingExpirationInMillis())).quantity(productBooking.quantity()).build());
        bookingRedisRepository.save(new BookingExpirationNotification(booking.getId()));
        return booking.getId();
    }
}
