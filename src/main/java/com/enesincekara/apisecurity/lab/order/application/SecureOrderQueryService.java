package com.enesincekara.apisecurity.lab.order.application;


import com.enesincekara.apisecurity.lab.order.domain.CustomerOrder;
import com.enesincekara.apisecurity.lab.order.repository.CustomerOrderRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class SecureOrderQueryService {

    private final CustomerOrderRepository repository;

    public SecureOrderQueryService(CustomerOrderRepository repository) {
        this.repository = repository;
    }

    public Optional<CustomerOrder> findAccessibleOrder(
            UUID authenticatedUserId,
            UUID orderId
    ) {

        Objects.requireNonNull(
                authenticatedUserId,
                "authenticatedUserId must not be null"
        );

        Objects.requireNonNull(
                orderId,
                "orderId must not be null"
        );
        return repository.findById(orderId)
                .filter(order-> order.belongsTo(authenticatedUserId));
    }
}
