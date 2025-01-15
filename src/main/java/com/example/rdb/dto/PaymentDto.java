package com.example.rdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto {
    @JsonProperty("method")
    private String method;

    @JsonProperty("transaction_id")
    private String transactionId;

    @JsonProperty("amount")
    private Amount amount;

    @JsonProperty("card_details")
    private CardDetails cardDetails;

    @Getter
    @Setter
    public static class Amount {
        @JsonProperty("total")
        private double total;

        @JsonProperty("currency")
        private String currency;
    }

    @Getter
    @Setter
    public static class CardDetails {
        @JsonProperty("type")
        private String type;

        @JsonProperty("last_four_digits")
        private String lastFourDigits;

        @JsonProperty("expiry_date")
        private String expiryDate;
    }
}