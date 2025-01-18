package com.example.rdb.controller;

import com.example.rdb.dto.ReceiptDto;
import com.example.rdb.service.ReceiptService;
import com.example.rdb.service.ReceiptServiceDenormalized;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptServiceDenormalized receiptService;

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDto> get(@PathVariable String id) {
        var got = receiptService.getById(id);
        if (got == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity.ok(got);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<List<ReceiptDto>> getReceiptsByCustomerId(@PathVariable String id) {
        var got = receiptService.getByCustomerId(id);
        if (got == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity.ok(got);
    }

    @GetMapping("/countCompletedGreaterThan")
    public ResponseEntity<Integer> countFinishedGreaterThan(@RequestParam("x") double x) {
        return ResponseEntity.ok(receiptService.countFinishedGreaterThan(x));
    }
}
