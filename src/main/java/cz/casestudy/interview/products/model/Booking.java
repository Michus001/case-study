package cz.casestudy.interview.products.model;

import cz.casestudy.interview.products.api.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a booking of a product.
 */
@Entity
@Table(name = "booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "booking_id")
    private UUID id;

    @Column
    private Instant expiresAt;

    @Column
    private Integer quantity;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id")
    private Product product;

    @Column
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
