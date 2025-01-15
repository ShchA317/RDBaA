package com.example.rdb.service;

import com.example.rdb.dto.CustomerDto;
import com.example.rdb.dto.OrderDto;
import com.example.rdb.dto.PaymentDto;
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

    @Transactional
    public void saveReceiptsBatch(List<ReceiptDto> receipts) {
        List<CustomerDto> customers = receipts.stream().map(it -> it.getCustomer()).toList();
        List<PaymentDto> payments = receipts.stream().map(it -> it.getPayment()).toList();
        List<OrderDto> orders = receipts.stream().map(it -> it.getOrder()).toList();

        repository.saveCustomersBatch(customers);
        repository.savePaymentsBatch(payments);
        repository.saveOrdersBatch(orders);
        repository.saveReceiptsBatch(receipts);
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
