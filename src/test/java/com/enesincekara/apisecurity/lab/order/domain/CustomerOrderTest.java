package com.enesincekara.apisecurity.lab.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderTest {

    @Test
    void shouldBelongToItsOwner(){
        UUID ownerId = UUID.randomUUID();

        CustomerOrder order = new CustomerOrder(
                UUID.randomUUID(),
                ownerId,
                "Mekanik Klavye",
                new BigDecimal("2499.90"),
                "TRY",
                Instant.now()
        );

        assertTrue(order.belongsTo(ownerId));
    }

    @Test
    void shouldNotBelongToAnotherUser() {
        UUID ownerId = UUID.randomUUID();
        UUID anotherUserId = UUID.randomUUID();

        CustomerOrder order = new CustomerOrder(
                UUID.randomUUID(),
                ownerId,
                "Mekanik Klavye",
                new BigDecimal("2499.90"),
                "TRY",
                Instant.now()
        );

        assertFalse(order.belongsTo(anotherUserId));
    }

    @Test
    void shouldRejectNonPositiveTotalAmount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerOrder(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "Mekanik Klavye",
                        BigDecimal.ZERO,
                        "TRY",
                        Instant.now()
                )
        );
    }
}