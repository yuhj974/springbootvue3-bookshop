package com.bookserve.controller;

import com.bookserve.exception.BusinessException;
import com.bookserve.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {
    @Autowired
    private StatsService statsService;

    @GetMapping("/category")
    public ResponseEntity<?> getCategoryStats() {
        try {
            return ResponseEntity.ok(statsService.getCategoryStats());
        } catch (BusinessException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/general")
    public ResponseEntity<?> getGeneralStats() {
        try {
            return ResponseEntity.ok(statsService.getGeneralStats());
        } catch (BusinessException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
