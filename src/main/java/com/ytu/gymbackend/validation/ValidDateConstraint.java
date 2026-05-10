package com.ytu.gymbackend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDateConstraint implements ConstraintValidator<ValidDate, String> {

    // dd/MM/yyyy where yyyy is 19xx or 20xx, leap-year aware.
    private static final String DATE_REGEX =
            "^(?:" +
            // 29/02/leap-year
            "(?:29/02/(?:19|20)(?:0[48]|[2468][048]|[13579][26]))" +
            // 31-day months
            "|(?:(?:0[1-9]|[12]\\d|3[01])/(?:01|03|05|07|08|10|12)/(?:19|20)\\d{2})" +
            // 30-day months
            "|(?:(?:0[1-9]|[12]\\d|30)/(?:04|06|09|11)/(?:19|20)\\d{2})" +
            // Feb 1-28
            "|(?:(?:0[1-9]|1\\d|2[0-8])/02/(?:19|20)\\d{2})" +
            ")$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        String trimmed = value.trim();
        if (trimmed.length() != 10) return false;
        return trimmed.matches(DATE_REGEX);
    }
}
