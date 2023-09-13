package org.acme;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StackListValidator.class)
@Documented
public @interface ValidStackList {
    String message() default "Each element should have at most 32 characters.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
