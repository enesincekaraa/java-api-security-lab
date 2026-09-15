package com.enesincekara.apisecurity.lab.order.repository;

import com.enesincekara.apisecurity.lab.order.domain.CustomerOrder;

import java.util.Optional;
import java.util.UUID;

public interface CustomerOrderRepository {

    Optional<CustomerOrder>  findById(UUID orderId);
}
