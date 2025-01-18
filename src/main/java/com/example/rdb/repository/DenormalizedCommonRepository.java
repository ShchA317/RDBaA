package com.example.rdb.repository;

import com.example.rdb.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DenormalizedCommonRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void saveReceiptsBatch(List<ReceiptDto> receiptsBatch) {
        String sql = "INSERT INTO denormalized_receipt (id, date, status, transaction_id, order_id, customer_id, tracking_number, " +
                "method, amount, currency, card_type, card_last_four_digits, card_expiry_date, " +
                "item, shipping_address," +
                "name, email, phone, loyalty_points_earned, loyalty_points_balance) " +

                "VALUES (:id, :date, :status, :transactionId, :orderId, :customerId, :trackingNumber, " +
                ":method, :amount, :currency, :cardType, :lastFourDigits, :expiryDate, " +
                "CAST(:item AS jsonb), CAST(:shippingAddress AS jsonb), " +
                ":name, :email, :phone, :pointsEarned, :pointsBalance) " +
                "ON CONFLICT DO NOTHING";

        List<SqlParameterSource> batchParams = new ArrayList<>(receiptsBatch.size());

        for (var receiptDto : receiptsBatch) {
            var payment = receiptDto.getPayment();
            var order = receiptDto.getOrder();
            var customer = receiptDto.getCustomer();

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", receiptDto.getId())
                    .addValue("date", receiptDto.getDate())
                    .addValue("status", receiptDto.getStatus())
                    .addValue("transactionId", receiptDto.getPayment().getTransactionId())
                    .addValue("orderId", receiptDto.getOrder().getOrderId())
                    .addValue("customerId", receiptDto.getCustomer().getCustomerId())
                    .addValue("trackingNumber", receiptDto.getLogistics().getTrackingNumber())

                    .addValue("method", payment.getMethod())
                    .addValue("amount", payment.getAmount().getTotal())
                    .addValue("currency", payment.getAmount().getCurrency())
                    .addValue("cardType", payment.getCardDetails().getType())
                    .addValue("lastFourDigits", payment.getCardDetails().getLastFourDigits())
                    .addValue("expiryDate", payment.getCardDetails().getExpiryDate())

                    .addValue("item", objectMapper.writeValueAsString(order.getItems()))
                    .addValue("shippingAddress", objectMapper.writeValueAsString(order.getShippingAddress()))

                    .addValue("name", customer.getName())
                    .addValue("email", customer.getEmail())
                    .addValue("phone", customer.getPhone())
                    .addValue("pointsEarned", customer.getLoyalty().getPointsEarned())
                    .addValue("pointsBalance", customer.getLoyalty().getCurrentBalance());

            batchParams.add(params);
        }

        SqlParameterSource[] batchArray = batchParams.toArray(new SqlParameterSource[0]);
        jdbcTemplate.batchUpdate(sql, batchArray);
    }

    public int countFinishedGreaterThan(double x) {
        String sql = "select count(*) from denormalized_receipt WHERE status = 'PAID' and amount > :x";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("x", x);
        Integer result = jdbcTemplate.queryForObject(sql, params, Integer.class);
        if (result == null) {
            return 0;
        }
        return result;
    }

    public ReceiptDto getReceiptById(String receiptId) {
        String sql = "SELECT * FROM denormalized_receipt " +
                "WHERE id = :receiptId";
        var params = new MapSqlParameterSource("receiptId", receiptId);
        return jdbcTemplate.queryForObject(sql, params, this::mapreceiptDto);
    }

    public List<ReceiptDto> getReceiptByCustomerId(String customerId) {
        String sql = "SELECT * FROM denormalized_receipt " +
                "WHERE customer_id = :customerId";
        var params = new MapSqlParameterSource("customerId", customerId);
        return jdbcTemplate.query(sql, params, this::mapreceiptDto);
    }

    private ReceiptDto mapreceiptDto(ResultSet rs, int rowNum) throws SQLException {
        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setId(rs.getString("id"));
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
            orderDto.setItems(objectMapper.readValue(rs.getString("item"), objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, OrderDto.ItemDto.class)));
            orderDto.setShippingAddress(objectMapper.readValue(rs.getString("shipping_address"), OrderDto.ShippingAddress.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing JSON for order", e);
        }
        receiptDto.setOrder(orderDto);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(rs.getString("customer_id"));
        customerDto.setName(rs.getString("name"));
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
    }
}
