package com.example.rdb.service;

import com.example.rdb.dto.ReceiptDto;
import com.example.rdb.repository.CassandraCommonRepository;
import com.example.rdb.repository.CassandraReceipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptCassandraService {
    private final CassandraCommonRepository repository;

    @Transactional
    public void saveReceiptsBatch(List<ReceiptDto> receiptDtos) {
        var toSave = receiptDtos.stream().map(CassandraReceipt::fromReceiptDto).toList();
        repository.saveAll(toSave);
    }

    public ReceiptDto getById(String id) {
        return repository.findById(id).orElseThrow().toReceiptDto();
    }

    public List<ReceiptDto> getByCustomerId(String customerId) {
        return repository.findAllByCustomerId(customerId)
                .stream()
                .map(CassandraReceipt::toReceiptDto)
                .toList();
    }

    public int countFinishedGreaterThan(double x) {
        var all = repository.findAll();
        int count = 0;
        for (var element : all) {
            if (element.getAmount() > x && element.getStatus().equals("PAID")) {
                count++;
            }
        }
        return count;
    }
}
