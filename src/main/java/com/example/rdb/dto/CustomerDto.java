package com.example.rdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("loyalty")
    private Loyalty loyalty;

    @Getter
    @Setter
    public static class Loyalty {
        @JsonProperty("points_earned")
        private int pointsEarned;

        @JsonProperty("current_balance")
        private int currentBalance;
    }
}