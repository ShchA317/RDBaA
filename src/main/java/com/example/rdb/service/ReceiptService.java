package com.example.rdb.service;

import com.example.rdb.dto.ReceiptDto;
import com.example.rdb.repository.CommonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final CommonRepository repository;

    @Transactional
    public void saveReceipt(ReceiptDto receiptDto) {
        repository.saveCustomer(receiptDto.getCustomer());
        repository.savePayment(receiptDto.getPayment());
        repository.saveOrder(receiptDto.getOrder());
        repository.saveReceipt(receiptDto);
    }

    public ReceiptDto getById(String id) {
        return repository.getFullReceiptById(id);
    }

    public List<ReceiptDto> getByCustomerId(String customerId) {
        return repository.getReceiptsByCustomerId(customerId);
    }

    public int countFinishedGreaterThan(double x) {
        return repository.countFinishedGreaterThan(x);
    }
}
