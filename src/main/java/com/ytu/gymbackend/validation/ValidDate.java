package com.ytu.gymbackend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDateConstraint.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
    String message() default "wrong_date_format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
