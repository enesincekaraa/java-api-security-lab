package com.enesincekara.apisecurity.lab.order.repository;

import com.enesincekara.apisecurity.lab.order.domain.CustomerOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JdbcCustomerOrderRepositoryTest {

    private static final UUID ALICE_ORDER_ID =
            UUID.fromString(
                    "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"
            );

    private final CustomerOrderRepository repository;

    @Autowired
    JdbcCustomerOrderRepositoryTest(CustomerOrderRepository repository) {
        this.repository = repository;
    }

    @Test
    void shouldFindExistingOrder(){

        Optional<CustomerOrder> result = repository.findById(ALICE_ORDER_ID);

        assertTrue(result.isPresent());

        CustomerOrder order = result.orElseThrow();

        assertEquals("Mekanik Klavye", order.getProductName());
        assertEquals(
                UUID.fromString(
                        "11111111-1111-1111-1111-111111111111"
                ),
                order.getOwnerId()
        );
    }

    @Test
    void shouldReturnEmptyWhenOrderDoesNotExist() {
        UUID unknownOrderId = UUID.randomUUID();

        Optional<CustomerOrder> result =
                repository.findById(unknownOrderId);

        assertTrue(result.isEmpty());
    }
}