package com.example.demo_bank.service;

import com.example.demo_bank.dto.BankResponse;
import com.example.demo_bank.dto.UserRequest;
import org.springframework.stereotype.Service;


public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

}
