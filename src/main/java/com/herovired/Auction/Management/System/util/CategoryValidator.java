package com.herovired.Auction.Management.System.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryValidator implements ConstraintValidator<ValidCategory, String> {

    private List<String> validCategories;

    @Override
    public void initialize(ValidCategory constraintAnnotation) {
        validCategories = Arrays.stream(Categories.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        String lowercaseValue = value.toLowerCase();
        List<String> lowercaseValidCategories = validCategories.stream()
                .map(String::toLowerCase).toList();
        return lowercaseValidCategories.contains(lowercaseValue);
    }
}
