package ua.anton.tsa.testassignment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.apache.commons.lang3.StringUtils;
import ua.anton.tsa.testassignment.wire.request.ModifyUserRequest;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The annotated element must contain at least one non-whitespace character or be null. Accepts {@link CharSequence}.
 */
@Documented
@Constraint(validatedBy = {NotBlankNullable.NotBlankNullableValidator.class,
        NotBlankNullable.NotBlankNullableMapValidator.class})
@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankNullable {

    String message() default "must contain at least one non-whitespace character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] keys() default {};

    /**
     * Validator for {@link CharSequence} object.
     * Checks if the object is null or is not blank
     */
    class NotBlankNullableValidator implements ConstraintValidator<NotBlankNullable, CharSequence> {

        @Override
        public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
            return Objects.isNull(value) || StringUtils.isNotBlank(value);
        }

    }

    /**
     * Validator for {@link ModifyUserRequest} object.
     * Checks if the object contains keys from {@link NotBlankNullable#keys()} and then if the values are not blank
     */
    class NotBlankNullableMapValidator implements ConstraintValidator<NotBlankNullable, ModifyUserRequest> {

        private List<String> keys;

        @Override
        public void initialize(NotBlankNullable constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
            this.keys = Arrays.stream(constraintAnnotation.keys()).toList();
        }

        @Override
        public boolean isValid(ModifyUserRequest value, ConstraintValidatorContext context) {
            StringBuilder errors = new StringBuilder();
            boolean isValid = true;
            for (String key : keys) {
                if (value.containsKey(key)) {
                    if (value.get(key).isBlank()) {
                        isValid = false;
                        errors
                                .append(key)
                                .append(" must not be blank, ");
                    }
                }
            }

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(errors.deleteCharAt(errors.lastIndexOf(", ")).toString())
                        .addConstraintViolation();
                return false;
            }

            return true;

        }

    }

}
