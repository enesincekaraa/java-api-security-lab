package com.enesincekara.apisecurity.lab.order.repository;

import com.enesincekara.apisecurity.lab.order.domain.CustomerOrder;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcCustomerOrderRepository implements CustomerOrderRepository{

    private final JdbcClient jdbcClient;
    public JdbcCustomerOrderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }



    @Override
    public Optional<CustomerOrder> findById(UUID orderId) {
        String sql = """
                SELECT id,
                       owner_id,
                       product_name,
                       total_amount,
                       currency,
                       created_at
                FROM customer_orders
                WHERE id = :orderId
                """;


        return jdbcClient
                .sql(sql)
                .param("orderId", orderId)
                .query(this::mapRow)
                .optional();

    }

    private CustomerOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CustomerOrder(
                rs.getObject("id", UUID.class),
                rs.getObject("owner_id", UUID.class),
                rs.getString("product_name"),
                rs.getBigDecimal("total_amount"),
                rs.getString("currency").trim(),
                rs.getTimestamp("created_at").toInstant()
        );
    }
}
