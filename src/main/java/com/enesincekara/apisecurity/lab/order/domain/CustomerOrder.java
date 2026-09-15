package com.enesincekara.apisecurity.lab.order.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class CustomerOrder {

    private final UUID id;
    private final UUID ownerId;
    private final String productName;
    private final BigDecimal totalAmount;
    private final String currency;
    private final Instant createdAt;


    public CustomerOrder(UUID id, UUID ownerId, String productName, BigDecimal totalAmount, String currency, Instant createdAt) {
        this.id = Objects.requireNonNull(
                id,
                "id must not be null"
        );

        this.ownerId = Objects.requireNonNull(
                ownerId,
                "ownerId must not be null"
        );

        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException(
                    "productName must not be blank"
            );
        }

        if (totalAmount == null || totalAmount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "totalAmount must be greater than zero"
            );
        }

        if (currency == null || !currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException(
                    "currency must contain three uppercase letters"
            );
        }

        this.productName = productName;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.createdAt = Objects.requireNonNull(
                createdAt,
                "createdAt must not be null"
        );
    }

    public boolean belongsTo(UUID userId) {
        return ownerId.equals(userId);
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
