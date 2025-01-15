package com.example.rdb.repository;

import com.example.rdb.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CommonRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public ReceiptDto getFullReceiptById(String receiptId) {
        String sql = "SELECT r.id as receipt_id, r.date, r.status, p.transaction_id, p.method, p.amount, p.currency, " +
                "p.card_type, p.card_last_four_digits, p.card_expiry_date, " +
                "o.id as order_id, o.item as order_items, o.shipping_address, " +
                "c.customer_id, c.name as customer_name, c.email, c.phone, c.loyalty_points_earned, c.loyalty_points_balance, " +
                "r.tracking_number " +
                "FROM receipt r " +
                "JOIN payment p ON r.transaction_id = p.transaction_id " +
                "JOIN \"order\" o ON r.order_id = o.id " +
                "JOIN customer c ON r.customer_id = c.customer_id " +
                "WHERE r.id = :receiptId";

        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("receiptId", receiptId), (rs, rowNum) -> {
            ReceiptDto receiptDto = new ReceiptDto();
            receiptDto.setId(rs.getString("receipt_id"));
            receiptDto.setDate(rs.getTimestamp("date").toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            receiptDto.setStatus(rs.getString("status"));

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setTransactionId(rs.getString("transaction_id"));
            paymentDto.setMethod(rs.getString("method"));

            PaymentDto.Amount amount = new PaymentDto.Amount();
            amount.setTotal(rs.getDouble("amount"));
            amount.setCurrency(rs.getString("currency"));
            paymentDto.setAmount(amount);

            PaymentDto.CardDetails cardDetails = new PaymentDto.CardDetails();
            cardDetails.setType(rs.getString("card_type"));
            cardDetails.setLastFourDigits(rs.getString("card_last_four_digits"));
            cardDetails.setExpiryDate(rs.getString("card_expiry_date"));
            paymentDto.setCardDetails(cardDetails);

            receiptDto.setPayment(paymentDto);

            OrderDto orderDto = new OrderDto();
            orderDto.setOrderId(rs.getString("order_id"));
            try {
                orderDto.setItems(objectMapper.readValue(rs.getString("order_items"), objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, OrderDto.ItemDto.class)));
                orderDto.setShippingAddress(objectMapper.readValue(rs.getString("shipping_address"), OrderDto.ShippingAddress.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error deserializing JSON for order", e);
            }
            receiptDto.setOrder(orderDto);

            CustomerDto customerDto = new CustomerDto();
            customerDto.setCustomerId(rs.getString("customer_id"));
            customerDto.setName(rs.getString("customer_name"));
            customerDto.setEmail(rs.getString("email"));
            customerDto.setPhone(rs.getString("phone"));

            CustomerDto.Loyalty loyalty = new CustomerDto.Loyalty();
            loyalty.setPointsEarned(rs.getInt("loyalty_points_earned"));
            loyalty.setCurrentBalance(rs.getInt("loyalty_points_balance"));
            customerDto.setLoyalty(loyalty);
            receiptDto.setCustomer(customerDto);

            LogisticsDto logisticsDto = new LogisticsDto();
            logisticsDto.setTrackingNumber(rs.getString("tracking_number"));
            receiptDto.setLogistics(logisticsDto);

            return receiptDto;
        });
    }

    // Retrieve all ReceiptDetails by customerId
    public List<ReceiptDto> getReceiptsByCustomerId(String customerId) {
        String sql = "SELECT r.id as receipt_id, r.date, r.status, p.transaction_id, p.method, p.amount, p.currency, " +
                "p.card_type, p.card_last_four_digits, p.card_expiry_date, " +
                "o.id as order_id, o.item as order_items, o.shipping_address, " +
                "c.customer_id, c.name as customer_name, c.email, c.phone, c.loyalty_points_earned, c.loyalty_points_balance, " +
                "r.tracking_number " +
                "FROM receipt r " +
                "JOIN payment p ON r.transaction_id = p.transaction_id " +
                "JOIN \"order\" o ON r.order_id = o.id " +
                "JOIN customer c ON r.customer_id = c.customer_id " +
                "WHERE c.customer_id = :customerId";

        return jdbcTemplate.query(sql, new MapSqlParameterSource("customerId", customerId), (rs, rowNum) -> {
            ReceiptDto receiptDto = new ReceiptDto();
            receiptDto.setId(rs.getString("receipt_id"));
            receiptDto.setDate(rs.getTimestamp("date").toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            receiptDto.setStatus(rs.getString("status"));

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setTransactionId(rs.getString("transaction_id"));
            paymentDto.setMethod(rs.getString("method"));

            PaymentDto.Amount amount = new PaymentDto.Amount();
            amount.setTotal(rs.getDouble("amount"));
            amount.setCurrency(rs.getString("currency"));
            paymentDto.setAmount(amount);

            PaymentDto.CardDetails cardDetails = new PaymentDto.CardDetails();
            cardDetails.setType(rs.getString("card_type"));
            cardDetails.setLastFourDigits(rs.getString("card_last_four_digits"));
            cardDetails.setExpiryDate(rs.getString("card_expiry_date"));
            paymentDto.setCardDetails(cardDetails);

            receiptDto.setPayment(paymentDto);

            OrderDto orderDto = new OrderDto();
            orderDto.setOrderId(rs.getString("order_id"));
            try {
                orderDto.setItems(objectMapper.readValue(rs.getString("order_items"), objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, OrderDto.ItemDto.class)));
                orderDto.setShippingAddress(objectMapper.readValue(rs.getString("shipping_address"), OrderDto.ShippingAddress.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error deserializing JSON for order", e);
            }
            receiptDto.setOrder(orderDto);

            CustomerDto customerDto = new CustomerDto();
            customerDto.setCustomerId(rs.getString("customer_id"));
            customerDto.setName(rs.getString("customer_name"));
            customerDto.setEmail(rs.getString("email"));
            customerDto.setPhone(rs.getString("phone"));

            CustomerDto.Loyalty loyalty = new CustomerDto.Loyalty();
            loyalty.setPointsEarned(rs.getInt("loyalty_points_earned"));
            loyalty.setCurrentBalance(rs.getInt("loyalty_points_balance"));
            customerDto.setLoyalty(loyalty);
            receiptDto.setCustomer(customerDto);

            LogisticsDto logisticsDto = new LogisticsDto();
            logisticsDto.setTrackingNumber(rs.getString("tracking_number"));
            receiptDto.setLogistics(logisticsDto);

            return receiptDto;
        });
    }

    public int countFinishedGreaterThan(double x) {
        String sql = "select count(*) from receipt r JOIN payment p ON r.transaction_id = p.transaction_id WHERE r.status = 'PAID' and p.amount > :x";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("x", x);
        Integer result = jdbcTemplate.queryForObject(sql, params, Integer.class);
        if (result == null) {
            return 0;
        }
        return result;
    }

    // RECEIPT
    public void saveReceipt(ReceiptDto receiptDto) {
        String sql = "INSERT INTO receipt (id, date, status, transaction_id, order_id, customer_id, tracking_number) " +
                "VALUES (:id, :date, :status, :transactionId, :orderId, :customerId, :trackingNumber) ON CONFLICT DO NOTHING";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", receiptDto.getId())
                .addValue("date", receiptDto.getDate())
                .addValue("status", receiptDto.getStatus())
                .addValue("transactionId", receiptDto.getPayment().getTransactionId())
                .addValue("orderId", receiptDto.getOrder().getOrderId())
                .addValue("customerId", receiptDto.getCustomer().getCustomerId())
                .addValue("trackingNumber", receiptDto.getLogistics().getTrackingNumber());

        jdbcTemplate.update(sql, params);
    }

    public ReceiptDto getReceiptById(String id) {
        String sql = "SELECT * FROM receipt WHERE id = :id";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id), this::mapreceiptDto);
    }

    private ReceiptDto mapreceiptDto(ResultSet rs, int rowNum) throws SQLException {
        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setId(rs.getString("id"));
        receiptDto.setDate(rs.getTimestamp("date").toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        receiptDto.setStatus(rs.getString("status"));
        receiptDto.setPayment(getPaymentByTransactionId(rs.getString("transaction_id")));
        receiptDto.setOrder(getOrderById(rs.getString("order_id")));
        receiptDto.setCustomer(getCustomerById(rs.getString("customer_id")));
        receiptDto.setLogistics(new LogisticsDto(rs.getString("tracking_number")));
        return receiptDto;
    }

    // PAYMENT
    public void savePayment(PaymentDto payment) {
        String sql = "INSERT INTO payment (transaction_id, method, amount, currency, card_type, card_last_four_digits, card_expiry_date) " +
                "VALUES (:transactionId, :method, :amount, :currency, :type, :lastFourDigits, :expiryDate) ON CONFLICT DO NOTHING";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("transactionId", payment.getTransactionId())
                .addValue("method", payment.getMethod())
                .addValue("amount", payment.getAmount().getTotal())
                .addValue("currency", payment.getAmount().getCurrency())
                .addValue("type", payment.getCardDetails().getType())
                .addValue("lastFourDigits", payment.getCardDetails().getLastFourDigits())
                .addValue("expiryDate", payment.getCardDetails().getExpiryDate());

        jdbcTemplate.update(sql, params);
    }

    public PaymentDto getPaymentByTransactionId(String transactionId) {
        String sql = "SELECT * FROM payment WHERE transaction_id = :transactionId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("transactionId", transactionId), this::mapPayment);
    }

    private PaymentDto mapPayment(ResultSet rs, int rowNum) throws SQLException {
        PaymentDto payment = new PaymentDto();
        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setMethod(rs.getString("method"));

        PaymentDto.Amount amount = new PaymentDto.Amount();
        amount.setTotal(rs.getDouble("amount"));
        amount.setCurrency(rs.getString("currency"));
        payment.setAmount(amount);

        PaymentDto.CardDetails cardDetails = new PaymentDto.CardDetails();
        cardDetails.setType(rs.getString("card_type"));
        cardDetails.setLastFourDigits(rs.getString("card_last_four_digits"));
        cardDetails.setExpiryDate(rs.getString("card_expiry_date"));
        payment.setCardDetails(cardDetails);

        return payment;
    }

    // ORDER
    public void saveOrder(OrderDto order) {
        String sql = "INSERT INTO \"order\" (id, item, shipping_address) VALUES (:orderId, CAST(:items AS jsonb), CAST(:shippingAddress AS jsonb)) ON CONFLICT DO NOTHING";

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("orderId", order.getOrderId());
            params.put("items", objectMapper.writeValueAsString(order.getItems()));
            params.put("shippingAddress", objectMapper.writeValueAsString(order.getShippingAddress()));

            jdbcTemplate.update(sql, params);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing order to JSON", e);
        }
    }

    public OrderDto getOrderById(String orderId) {
        String sql = "SELECT * FROM \"order\" WHERE id = :orderId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("orderId", orderId), this::mapOrder);
    }

    private OrderDto mapOrder(ResultSet rs, int rowNum) throws SQLException {
        OrderDto order = new OrderDto();
        order.setOrderId(rs.getString("id"));

        try {
            order.setItems(objectMapper.readValue(rs.getString("item"), objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, OrderDto.ItemDto.class)));
            order.setShippingAddress(objectMapper.readValue(rs.getString("shipping_address"), OrderDto.ShippingAddress.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing JSON", e);
        }

        return order;
    }

    // CUSTOMER
    public void saveCustomer(CustomerDto customer) {
        String sql = "INSERT INTO customer (customer_id, name, email, phone, loyalty_points_earned, loyalty_points_balance) " +
                "VALUES (:customerId, :name, :email, :phone, :pointsEarned, :pointsBalance) ON CONFLICT DO NOTHING";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerId", customer.getCustomerId())
                .addValue("name", customer.getName())
                .addValue("email", customer.getEmail())
                .addValue("phone", customer.getPhone())
                .addValue("pointsEarned", customer.getLoyalty().getPointsEarned())
                .addValue("pointsBalance", customer.getLoyalty().getCurrentBalance());

        jdbcTemplate.update(sql, params);
    }

    public CustomerDto getCustomerById(String customerId) {
        String sql = "SELECT * FROM customer WHERE customer_id = :customerId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("customerId", customerId), this::mapCustomer);
    }

    private CustomerDto mapCustomer(ResultSet rs, int rowNum) throws SQLException {
        CustomerDto customer = new CustomerDto();
        customer.setCustomerId(rs.getString("customer_id"));
        customer.setName(rs.getString("name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));

        CustomerDto.Loyalty loyalty = new CustomerDto.Loyalty();
        loyalty.setPointsEarned(rs.getInt("loyalty_points_earned"));
        loyalty.setCurrentBalance(rs.getInt("loyalty_points_balance"));
        customer.setLoyalty(loyalty);

        return customer;
    }
}
