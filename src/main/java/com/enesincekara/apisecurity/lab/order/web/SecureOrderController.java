package com.enesincekara.apisecurity.lab.order.web;

import com.enesincekara.apisecurity.lab.order.application.SecureOrderQueryService;
import com.enesincekara.apisecurity.lab.order.domain.CustomerOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lab/secure/orders")
public class SecureOrderController {

    private final SecureOrderQueryService service;

    public SecureOrderController(SecureOrderQueryService service) {
        this.service = service;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @RequestHeader("X-Lab-User-Id")
            UUID authenticatedUserId,
            @PathVariable("orderId")
            UUID orderId) {

        Optional<CustomerOrder> accessibleOrder =
                service.findAccessibleOrder(
                        authenticatedUserId, orderId
                );

        if (accessibleOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CustomerOrder order = accessibleOrder.orElseThrow();

        return ResponseEntity.ok(OrderResponse.from(order));

    }
}
