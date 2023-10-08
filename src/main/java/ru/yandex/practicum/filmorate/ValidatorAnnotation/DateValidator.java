package ru.yandex.practicum.filmorate.ValidatorAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<ValidateDate, LocalDate> {
    private static final LocalDate LIMIT_DATE = LocalDate.of(1895, 12, 28);
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LIMIT_DATE);
    }
}
