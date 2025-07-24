package com.example.armstrong.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ArmstrongService {

    private static final Logger logger = LoggerFactory.getLogger(ArmstrongService.class);

    public boolean isArmstrongNumber(int number) {
        if (number < 0) {
            return false; // Negative numbers are not Armstrong numbers.
        }

        int originalNumber = number;
        int sum = 0;
        int numDigits = String.valueOf(number).length();

        while (number > 0) {
            int digit = number % 10;
            sum += Math.pow(digit, numDigits);
            number /= 10;
        }

        return sum == originalNumber;
    }

    public String getArmstrongEquation(int number) {
        if (number < 0) {
            return "Invalid input: Number must be non-negative.";
        }

        int originalNumber = number;
        int numDigits = String.valueOf(number).length();
        StringBuilder equation = new StringBuilder();
        int tempNumber = number;

        for (int i = 0; i < numDigits; i++) {
            int digit = tempNumber % 10;
            equation.insert(0, digit + "^" + numDigits);
            if (i < numDigits - 1) {
                equation.insert(0, " + ");
            }
            tempNumber /= 10;
        }

        equation.append(" = ").append(originalNumber);
        return equation.toString();
    }
}