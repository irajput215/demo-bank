package com.example.demo_bank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already exists.";
    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "ccount has been successfully created!";

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
