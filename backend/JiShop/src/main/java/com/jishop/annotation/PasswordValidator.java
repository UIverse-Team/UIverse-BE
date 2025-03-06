package com.jishop.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.regex.Pattern;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final int MIN_SIZE = 8;
    private static final int MAX_SIZE = 20;
    private static final String regexPassword =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z0-9$@$!%*#?&]{" + MIN_SIZE + ","
            + MAX_SIZE + "}$";

    private static final Pattern PATTERN = Pattern.compile(regexPassword);


    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(!matches(password)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("{0}자 이상의 숫자, 영문자, 특수문자를 {1}자씩은 포함한 비밀번호를 입력해주세요!"
                    , MIN_SIZE, MAX_SIZE)).addConstraintViolation();
            return false;
        }
        return true;
    }

    public boolean matches(String password) {
        return PATTERN.matcher(password).matches();
    }
}
