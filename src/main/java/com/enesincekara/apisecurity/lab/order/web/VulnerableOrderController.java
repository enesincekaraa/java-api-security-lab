package com.enesincekara.apisecurity.lab.order.web;


import com.enesincekara.apisecurity.lab.order.application.VulnerableOrderQueryService;
import com.enesincekara.apisecurity.lab.order.domain.CustomerOrder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lab/vulnerable/orders")
public class VulnerableOrderController {

    private final VulnerableOrderQueryService service;
    public VulnerableOrderController(VulnerableOrderQueryService service) {
        this.service = service;
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(
            @RequestHeader("X-Lab-User-Id")UUID authenticatedUserId,
            @PathVariable UUID orderId
            ){
        CustomerOrder order = service.getOrder(authenticatedUserId, orderId);

        return OrderResponse.from(order);
    }
}
