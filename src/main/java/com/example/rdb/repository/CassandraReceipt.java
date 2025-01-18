package com.example.rdb.repository;

import com.example.rdb.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Table("denormalized_receipt")
@Getter
@Setter
public class CassandraReceipt {
    @PrimaryKey
    private String id;

    @Column("date")
    private LocalDateTime date;

    @Column("status")
    private String status;

    @Column("transaction_id")
    private String transactionId;

    @Column("order_id")
    private String orderId;

    @Column("customer_id")
    private String customerId;

    @Column("tracking_number")
    private String trackingNumber;

    @Column("method")
    private String method;

    @Column("amount")
    private double amount;

    @Column("currency")
    private String currency;

    @Column("card_type")
    private String cardType;

    @Column("card_last_four_digits")
    private String cardLastFourDigits;

    @Column("card_expiry_date")
    private String cardExpiryDate;

    @Column("item")
    private String item;

    @Column("shipping_address")
    private String shippingAddress;

    @Column("name")
    private String name;

    @Column("email")
    private String email;

    @Column("phone")
    private String phone;

    @Column("loyalty_points_earned")
    private int loyaltyPointsEarned;

    @Column("loyalty_points_balance")
    private int loyaltyPointsBalance;

    @SneakyThrows
    public ReceiptDto toReceiptDto() {
        var objectMapper = new ObjectMapper();
        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setId(id);
        receiptDto.setDate(date);
        receiptDto.setStatus(status);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setTransactionId(transactionId);
        paymentDto.setMethod(method);

        PaymentDto.Amount amount = new PaymentDto.Amount();
        amount.setTotal(this.amount);
        amount.setCurrency(currency);
        paymentDto.setAmount(amount);

        PaymentDto.CardDetails cardDetails = new PaymentDto.CardDetails();
        cardDetails.setType(cardType);
        cardDetails.setLastFourDigits(cardLastFourDigits);
        cardDetails.setExpiryDate(cardExpiryDate);
        paymentDto.setCardDetails(cardDetails);

        receiptDto.setPayment(paymentDto);

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(orderId);
        orderDto.setItems(objectMapper.readValue(item, objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, OrderDto.ItemDto.class)));
        orderDto.setShippingAddress(objectMapper.readValue(shippingAddress, OrderDto.ShippingAddress.class));

        receiptDto.setOrder(orderDto);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customerId);
        customerDto.setName(name);
        customerDto.setEmail(email);
        customerDto.setPhone(phone);

        CustomerDto.Loyalty loyalty = new CustomerDto.Loyalty();
        loyalty.setPointsEarned(loyaltyPointsEarned);
        loyalty.setCurrentBalance(loyaltyPointsBalance);
        customerDto.setLoyalty(loyalty);
        receiptDto.setCustomer(customerDto);

        LogisticsDto logisticsDto = new LogisticsDto();
        logisticsDto.setTrackingNumber(trackingNumber);
        receiptDto.setLogistics(logisticsDto);

        return receiptDto;
    }

    @SneakyThrows
    public static CassandraReceipt fromReceiptDto(ReceiptDto receiptDto) {
        var objectMapper = new ObjectMapper();
        var receipt = new CassandraReceipt();

        receipt.setId(receiptDto.getId());
        receipt.setDate(receiptDto.getDate());
        receipt.setStatus(receiptDto.getStatus());

        receipt.setTransactionId(receiptDto.getPayment().getTransactionId());
        receipt.setMethod(receiptDto.getPayment().getMethod());
        receipt.setAmount(receiptDto.getPayment().getAmount().getTotal());
        receipt.setCurrency(receiptDto.getPayment().getAmount().getCurrency());
        receipt.setCardType(receiptDto.getPayment().getCardDetails().getType());
        receipt.setCardLastFourDigits(receiptDto.getPayment().getCardDetails().getLastFourDigits());
        receipt.setCardExpiryDate(receiptDto.getPayment().getCardDetails().getExpiryDate());

        receipt.setOrderId(receiptDto.getOrder().getOrderId());
        receipt.setItem(objectMapper.writeValueAsString(receiptDto.getOrder().getItems()));
        receipt.setShippingAddress(objectMapper.writeValueAsString(receiptDto.getOrder().getShippingAddress()));


        receipt.setCustomerId(receiptDto.getCustomer().getCustomerId());
        receipt.setName(receiptDto.getCustomer().getName());
        receipt.setEmail(receiptDto.getCustomer().getEmail());
        receipt.setPhone(receiptDto.getCustomer().getPhone());
        receipt.setLoyaltyPointsEarned(receiptDto.getCustomer().getLoyalty().getPointsEarned());
        receipt.setLoyaltyPointsBalance(receiptDto.getCustomer().getLoyalty().getCurrentBalance());

        return receipt;
    }
}
