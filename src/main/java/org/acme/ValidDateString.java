package org.acme;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateStringValidator.class)
@Documented
public @interface ValidDateString {
    String message() default "Invalid date format. Please use YYYY-MM-DD.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
