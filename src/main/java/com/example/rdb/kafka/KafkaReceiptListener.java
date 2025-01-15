package com.example.rdb.kafka;

import com.example.rdb.dto.ReceiptDto;
import com.example.rdb.dto.ReceiptHolder;
import com.example.rdb.service.ReceiptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaReceiptListener implements ConsumerSeekAware {
    private final ReceiptService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "receipts",
            groupId = "service_account",
            containerFactory = "kafkaListenerContainerFactory",
            concurrency = "16"
    )
    public void listen(@Payload List<String> rawReceipts) throws JsonProcessingException {
        for (String rawReceipt : rawReceipts) {
            ReceiptDto receiptDto = objectMapper.readValue(rawReceipt, ReceiptHolder.class).getReceipt();
            service.saveReceipt(receiptDto);
        }
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        assignments.forEach((t, o) -> callback.seekToBeginning(t.topic(), t.partition()));
    }
}
