package com.example.rdb.service;

import com.example.rdb.dto.ReceiptDto;
import com.example.rdb.repository.DenormalizedCommonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptServiceDenormalized {
    private final DenormalizedCommonRepository repository;

    @Transactional
    public void saveReceiptsBatch(List<ReceiptDto> receiptDtos) {
        repository.saveReceiptsBatch(receiptDtos);
    }

    public ReceiptDto getById(String id) {
        return repository.getReceiptById(id);
    }

    public List<ReceiptDto> getByCustomerId(String customerId) {
        return repository.getReceiptByCustomerId(customerId);
    }

    public int countFinishedGreaterThan(double x) {
        return repository.countFinishedGreaterThan(x);
    }
}
