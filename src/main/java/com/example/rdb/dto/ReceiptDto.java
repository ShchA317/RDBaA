package com.example.rdb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReceiptDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]")
    private LocalDateTime date;

    @JsonProperty("status")
    private String status;

    @JsonProperty("payment")
    private PaymentDto payment;

    @JsonProperty("order")
    private OrderDto order;

    @JsonProperty("customer")
    private CustomerDto customer;

    @JsonProperty("logistics")
    private LogisticsDto logistics;
}
