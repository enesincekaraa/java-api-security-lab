package com.enesincekara.apisecurity.lab.order.web;

import com.enesincekara.apisecurity.lab.order.domain.CustomerOrder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID ownerId,
        String productName,
        BigDecimal totalAmount,
        String currency,
        Instant createdAt
) {

    public static OrderResponse from(CustomerOrder order){
        return new OrderResponse(
                order.getId(),
                order.getOwnerId(),
                order.getProductName(),
                order.getTotalAmount(),
                order.getCurrency(),
                order.getCreatedAt()
        );
    }
}
