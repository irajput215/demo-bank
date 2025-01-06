package com.example.demo_bank.service;

import com.example.demo_bank.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
