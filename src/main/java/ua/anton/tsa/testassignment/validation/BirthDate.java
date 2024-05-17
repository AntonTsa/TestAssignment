package ua.anton.tsa.testassignment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.SneakyThrows;
import ua.anton.tsa.testassignment.wire.request.ModifyUserRequest;

import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * An annotation for birthDate validation
 * The annotated element must be (or contain) a valid local date earlier than current day.
 * Accepts {@link LocalDate} or {@link ModifyUserRequest} objects
 */
@Documented
@Constraint(validatedBy = {BirthDate.BirthDateValidator.class, BirthDate.BirthDateInMapValidator.class})
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

    /**
     * Validator for {@link LocalDate} object.
     * Checks if the object is not null and if the date that is before the current date.
     */
    class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {

        @Override
        public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
            try {
                return value != null && value.isBefore(LocalDate.now());
            } catch (Exception e) {
                return false;
            }
        }

    }

    /**
     * Validator for {@link ModifyUserRequest} object.
     * Checks if the object contains a key "birthDate" with the date that is before the current date.
     */
    class BirthDateInMapValidator implements ConstraintValidator<BirthDate, ModifyUserRequest> {

        @Override
        @SneakyThrows(DateTimeParseException.class)
        public boolean isValid(ModifyUserRequest modifyUserRequest, ConstraintValidatorContext context) {
            if (modifyUserRequest != null && modifyUserRequest.containsKey("birthDate")) {
                try {
                    boolean isValid = LocalDate.parse(modifyUserRequest.get("birthDate")).isBefore(LocalDate.now());

                    if (!isValid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                                .addPropertyNode("birthDate")
                                .addConstraintViolation();
                        return false;
                    }
                } catch (Exception e) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                            .addPropertyNode("birthDate")
                            .addConstraintViolation();
                    return false;
                }
            }
            return true;
        }
    }
}
