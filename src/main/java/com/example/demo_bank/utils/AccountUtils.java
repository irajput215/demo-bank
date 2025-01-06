package com.example.demo_bank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already exists.";
    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "account has been successfully created!";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "account number does not exist!!";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "here is the account info";
    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "account credited!";
    public static final String ACCOUNT_DEBITED_SUCCESS = "006";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "account debited!";
    public static final String ACCOUNT_DEBITED_NOT_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_NOT_SUCCESS_MESSAGE = "account balance too low!";

    public static final String TRANSFER_SUCCESSFUL_CODE = "008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer Successful";



    public static String generateAccountNumber(){
        /*
         * 2024 + randomSixDigits
         */
        Year currentYear = Year.now();

        int min = 100000;
        int max = 999999;
        // generate a random number between min and max
        int randNumber = (int) Math.floor(Math.random()*(max-min+1)+min);

        // convert the current year and randomNumber to the strings, and concatenate them

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();
    }


}
