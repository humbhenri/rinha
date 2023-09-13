package org.acme;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class StackListValidator implements ConstraintValidator<ValidStackList, List<String>> {
    private static final int MAX_LENGTH = 32;

    @Override
    public void initialize(ValidStackList constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are considered valid
        }

        for (String str : value) {
            if (str.length() > MAX_LENGTH) {
                return false;
            }
        }

        return true;
    }
}
