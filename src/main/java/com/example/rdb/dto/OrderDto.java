package com.example.rdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {
    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("items")
    private List<ItemDto> items;

    @JsonProperty("shipping_address")
    private ShippingAddress shippingAddress;


    @Getter
    @Setter
    public static class ItemDto {
        @JsonProperty("item_id")
        private String itemId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("quantity")
        private int quantity;

        @JsonProperty("unit_price")
        private double unitPrice;

        @JsonProperty("discount")
        private Double discount;

        @JsonProperty("total_price")
        private double totalPrice;
    }


    @Getter
    @Setter
    public static class ShippingAddress {
        @JsonProperty("recipient_name")
        private String recipientName;

        @JsonProperty("address_line1")
        private String addressLine1;

        @JsonProperty("address_line2")
        private String addressLine2;

        @JsonProperty("city")
        private String city;

        @JsonProperty("state")
        private String state;

        @JsonProperty("postal_code")
        private String postalCode;

        @JsonProperty("country")
        private String country;
    }
}
