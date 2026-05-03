package com.ytu.gymbackend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidTcKimlikNoConstraint.class)
@Documented
public @interface ValidTcKimlikNo {
    String message() default "Invalid TC Kimlik No";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}