package ua.anton.tsa.testassigment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The annotated element must be a valid local date earlier than current day or null. Accepts {@link CharSequence}.
 */
@Documented
@Constraint(validatedBy = BirthDateNullable.BirthDateNullableValidator.class)
@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDateNullable {
    String message() default "must be a valid local date earlier than current day or null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class BirthDateNullableValidator implements ConstraintValidator<BirthDateNullable, CharSequence> {

        @Override
        public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
            if (value != null) {
                try {
                    LocalDate birthdate = LocalDate.parse(value);
                    return birthdate.isBefore(LocalDate.now());
                } catch (Exception e) {
                    return false;
                }
            } else {
                return true;
            }
        }

    }
}
