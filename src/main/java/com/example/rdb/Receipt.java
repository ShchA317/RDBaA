package com.example.rdb;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long receipt_id;
    public String date;
    public String time;
    public StoreInfo store_info;
    public double subtotal;
    public Taxes taxes;
    public double total;
    public String payment_method;
    public String transaction_id;
    public String cashier;

    @ElementCollection
    public List<Item> items;

    @Embeddable
    public static class StoreInfo {
        public String name;
        public Address address;
        public String phone;
    }

    public static class Address {
        public String street;
        public String city;
        public String postal_code;
        public String country;
    }

    @Embeddable
    public static class Item {
        public String item_name;
        public int quantity;
        public double unit_price;
        public double total_price;
    }

    @Embeddable
    public static class Taxes {
        public int tax_rate;
        public double tax_amount;
    }
}

