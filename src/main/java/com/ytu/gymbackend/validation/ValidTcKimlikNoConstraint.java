package com.ytu.gymbackend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTcKimlikNoConstraint implements ConstraintValidator<ValidTcKimlikNo, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !value.matches("^[1-9]\\d{10}$")) return false;

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = value.charAt(i) - '0';
        }

        int sumOdd = digits[0] + digits[2] + digits[4] + digits[6] + digits[8];
        int sumEven = digits[1] + digits[3] + digits[5] + digits[7];

        int tenthDigit = (sumOdd * 7 - sumEven) % 10;
        if (tenthDigit < 0) tenthDigit += 10;

        if (digits[9] != tenthDigit) return false;

        int eleventhDigit = (sumOdd + sumEven + digits[9]) % 10;
        return digits[10] == eleventhDigit;
    }
}