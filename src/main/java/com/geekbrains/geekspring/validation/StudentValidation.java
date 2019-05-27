package com.geekbrains.geekspring.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StudentValidation implements ConstraintValidator<ValidStudent, Object> {
    private String email;
    private String phone;
    private String message;

    @Override
    public void initialize(ValidStudent constraintAnnotation) {
        email = constraintAnnotation.email();
        phone = constraintAnnotation.phone();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean valid = false;
        try {
            final Object emailObj = new BeanWrapperImpl(value).getPropertyValue(email);
            final Object phoneObj = new BeanWrapperImpl(value).getPropertyValue(phone);

            valid = (emailObj != null && phoneObj != null)
                    & email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
                    & phone.matches("(8|\\+7)\\d{10}");
        } catch (final Exception ignore) {
        }

        if (valid == false)  {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(email)
                    .addPropertyNode(phone )
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}
