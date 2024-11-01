package bookingapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldMatchesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FieldMatches {
    String message() default "Fields value doesn't match";
    String field();
    String fieldMatch();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
