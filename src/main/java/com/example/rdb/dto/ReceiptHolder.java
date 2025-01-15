package com.example.rdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ReceiptHolder {
    @JsonProperty("receipt")
    private ReceiptDto receipt;
}
