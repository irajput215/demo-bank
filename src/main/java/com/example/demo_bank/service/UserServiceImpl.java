package com.example.demo_bank.service;

import com.example.demo_bank.dto.*;
import com.example.demo_bank.entity.Transaction;
import com.example.demo_bank.entity.User;
import com.example.demo_bank.repository.TransactionRepository;
import com.example.demo_bank.repository.UserRepository;
import com.example.demo_bank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionService transactionService;


    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /*
        * Creating an account - saving a new user into the db
        * Check if user already exist
         */

        if (userRepository.existsByEmail(userRequest.getEmail())){
            BankResponse response = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
            return response;
        }


        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        // saving newUser to db
        User savedUser = userRepository.save(newUser);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        // check if the provided account number exists
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .accountName(foundUser.getFirstName()+" "+ foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        Boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return foundUser.getFirstName()+" "+ foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        // check if the account exist
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        // save transaction

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("Credit")
                .accountNumber(userToCredit.getAccountNumber())
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);
//--------------------------------------------------------------

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        // check if the account exist
        Boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigDecimal curBalance = userToCredit.getAccountBalance();

        if (curBalance.compareTo(request.getAmount())>=0){
            userToCredit.setAccountBalance(userToCredit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToCredit);

            // save transaction

            TransactionDto transactionDto = TransactionDto.builder()
                    .transactionType("Debit")
                    .accountNumber(userToCredit.getAccountNumber())
                    .amount(request.getAmount())
                    .build();

            transactionService.saveTransaction(transactionDto);
            //----------------------------------------------------

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToCredit.getFirstName()+ " " + userToCredit.getLastName())
                            .accountBalance(userToCredit.getAccountBalance())
                            .accountNumber(request.getAccountNumber())
                            .build())
                    .build();
        }
        else {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_NOT_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_NOT_SUCCESS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }





    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        // get the account for debit
        // check if it exists
        // debit the account
        // get the account for credit
        // check if it exists
        // credit the account
        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

//        if (!isSourceAccountExist){
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
//                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//        System.out.println("Source Account Number: " + request.getSourceAccountNumber());
//        System.out.println("Destination Account Number: " + request.getDestinationAccountNumber());
//
//        System.out.println(isSourceAccountExist );
//        System.out.println(isDestinationAccountExist);

        if (!isDestinationAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());

        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance())>0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_NOT_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_NOT_SUCCESS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);


        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        // save transaction

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("transfer")
                .accountNumber(destinationAccountUser.getAccountNumber())
                .amount(request.getAmount())
                .status(sourceAccountUser.getAccountNumber())
                .build();

        transactionService.saveTransaction(transactionDto);
//--------------------------------------------------------------


        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();

    }

}
