package com.example.rdb;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaReceiptListener {

    private final ReceiptRepository receiptRepository;

    public KafkaReceiptListener(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @KafkaListener(topics = "receipts_topic", groupId = "receipt-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(Receipt receipt) {
        System.out.println("Received receipt: " + receipt.getReceipt_id());
        receiptRepository.save(receipt);
    }
}
