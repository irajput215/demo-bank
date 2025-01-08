package com.example.demo_bank.service;

import com.example.demo_bank.dto.TransactionDto;
import com.example.demo_bank.entity.Transaction;
import com.example.demo_bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public void saveTransaction(TransactionDto transactionDto) {


        if (Objects.equals(transactionDto.getTransactionType(), "transfer")){
            Transaction transaction = Transaction.builder()
                    .transactionType(transactionDto.getTransactionType())
                    .accountNumber(transactionDto.getAccountNumber())
                    .amount(transactionDto.getAmount())
                    .tag(transactionDto.getTag())
                    .status(transactionDto.getStatus())
                    .build();

            transactionRepository.save(transaction);
        }
        else {
            Transaction transaction = Transaction.builder()
                    .transactionType(transactionDto.getTransactionType())
                    .accountNumber(transactionDto.getAccountNumber())
                    .amount(transactionDto.getAmount())
                    .tag(transactionDto.getTag())
                    .status("SUCCESS")
                    .build();

            transactionRepository.save(transaction);
        }
        System.out.println("Transaction saved successfully");
    }
}
