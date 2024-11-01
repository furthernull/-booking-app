package bookingapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface Password {
    String message() default "Password must contain at least one digit,"
            + " one lowercase letter, one uppercase letter, one special character"
            + " and must be at least 8 characters,";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
