package com.example.rdb.dto;

public record ReceiptResponseDto(
        Long receiptId,
        Long storeId,
        String timestamp,
        String totalAmount
) {
}
