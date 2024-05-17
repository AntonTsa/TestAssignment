package ua.anton.tsa.testassignment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import ua.anton.tsa.testassignment.wire.request.ModifyUserRequest;

import java.lang.annotation.*;
import java.util.regex.Pattern;

/**
 * An annotation for email validation
 * The annotated element must be (or contain) a not blank well-formed email address.
 * Accepts {@link CharSequence} or {@link ModifyUserRequest}
 */
@Documented
@Constraint(validatedBy = {
        Email.EmailValidator.class,
        Email.EmailMapValidator.class
})
@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default "must be a not blank well-formed email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Validator for {@link CharSequence} object.
     * Checks if the object is not null and if it matches a pattern.
     */
    class EmailValidator implements ConstraintValidator<Email, CharSequence> {

        @Override
        public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
            return value != null && Pattern.matches(
                    "^[a-zA-Z0-9_!#$%&’*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
                    value);
        }

    }

    /**
     * Validator for {@link ModifyUserRequest} object.
     * Checks if the object contains a key "email" with the value matches pattern.
     */
    class EmailMapValidator implements ConstraintValidator<Email, ModifyUserRequest> {

        @Override
        public boolean isValid(ModifyUserRequest modifyUserRequest, ConstraintValidatorContext context) {
            if (modifyUserRequest != null && modifyUserRequest.containsKey("email")) {
                boolean isValid = Pattern.matches(
                        "^[a-zA-Z0-9_!#$%&’*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
                        modifyUserRequest.get("email"));

                if (!isValid) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                            .addPropertyNode("email")
                            .addConstraintViolation();
                    return false;
                }
            }
            return true;
        }
    }
}
