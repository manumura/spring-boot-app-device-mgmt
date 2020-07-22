package com.manu.test.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * @author emmanuel.mura
 *
 */
public class ValidationUtil {

    private ValidationUtil() {}

    public static List<String> fromBindingErrors(Errors errors) {
        List<String> validErrors = new ArrayList<>();
        for (ObjectError objectError : errors.getAllErrors()) {
            validErrors.add(objectError.getDefaultMessage());
        }
        return validErrors;
    }
}
