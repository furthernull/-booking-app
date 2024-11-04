package bookingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<Date, Object> {
    private String startDate;
    private String endDate;

    @Override
    public void initialize(Date constraintAnnotation) {
        this.startDate = constraintAnnotation.startDate();
        this.endDate = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true;
        }

        try {
            Field startDate = obj.getClass().getDeclaredField(this.startDate);
            Field endDate = obj.getClass().getDeclaredField(this.endDate);
            startDate.setAccessible(true);
            endDate.setAccessible(true);

            LocalDate checkInDate = (LocalDate) startDate.get(obj);
            LocalDate checkOutDate = (LocalDate) endDate.get(obj);

            return checkInDate != null && checkInDate.isAfter(LocalDate.now())
                    && checkInDate.isBefore(checkOutDate);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
