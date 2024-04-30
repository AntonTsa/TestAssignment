package ua.anton.tsa.testassigment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The annotated element must be a valid local date earlier than current day. Accepts {@link CharSequence}.
 */
@Documented
@Constraint(validatedBy = BirthDate.BirthDateValidator.class)
@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDate {

    String message() default "must be a valid local date earlier than current day";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class BirthDateValidator implements ConstraintValidator<BirthDate, CharSequence> {

        @Override
        public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
            try {
                LocalDate birthdate = LocalDate.parse(value);
                return birthdate.isBefore(LocalDate.now());
            } catch (Exception e) {
                return false;
            }
        }

    }
}
