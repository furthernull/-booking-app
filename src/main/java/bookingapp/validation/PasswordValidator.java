package bookingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*=.+_\\-]).{8,25}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }
}
