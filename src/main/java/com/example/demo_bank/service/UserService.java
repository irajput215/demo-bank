package com.example.demo_bank.service;

import com.example.demo_bank.dto.BankResponse;
import com.example.demo_bank.dto.CreditDebitRequest;
import com.example.demo_bank.dto.EnquiryRequest;
import com.example.demo_bank.dto.UserRequest;
import org.springframework.stereotype.Service;


public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse debitAccount(CreditDebitRequest request);
}
