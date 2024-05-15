package ua.anton.tsa.testassignment.validation;

import jakarta.validation.*;

import java.lang.annotation.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Pattern;

@Documented
@Constraint(validatedBy = ValidParams.ValidParamsValidator.class)
@Target({
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.PARAMETER,
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidParams {
    String message() default "body invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ValidParamsValidator implements ConstraintValidator<ValidParams, Map<String, String>> {

        @Override
        public void initialize(ValidParams constraintAnnotation) {

        }

        @Override
        public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {

            if (value == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Body is null!");
                return false;
            } else {
                boolean isValid = true;
                StringBuilder errorMessage = new StringBuilder();
                for (String key: value.keySet()) {
                    switch (key) {
                        case "email" : {
                            String email = value.get("email");
                            if (value.get("email") == null || email.isEmpty() || !Pattern.matches(".*", email)) {
                                errorMessage.append("email must be not blank well-formed address; ");
                                isValid = false;
                            }
                            break;
                        }
                        case "firstName" : {
                            if (value.get("firstName") == null || value.get("firstName").isEmpty()) {
                                errorMessage.append("firstName must not be blank; ");
                                isValid = false;
                            }
                            break;
                        }
                        case "lastName" : {
                            if (value.get("lastName") == null || value.get("lastName").isEmpty()) {
                                errorMessage.append("lastName must not be blank; ");
                                isValid = false;
                            }
                            break;
                        }
                        case "birthDate" : {
                            if (value.get("birthDate") == null || !LocalDate.parse(value.get("birthDate")).isBefore(LocalDate.now())) {
                                errorMessage.append("birthDate must be a valid local date earlier than current day; ");
                                isValid = false;
                            }
                            break;
                        }
                        case "address" : {
                            if (value.get("phoneNumber") != null && value.get("address").isEmpty()) {
                                errorMessage.append("address must not be blank; ");
                                isValid = false;
                            }
                            break;
                        }
                        case "phoneNumber" : {

                            if (value.get("phoneNumber") != null && value.get("phoneNumber").isEmpty()) {
                                errorMessage.append("phoneNumber must not be blank; ");
                                isValid = false;
                            }
                            break;
                        }
                        default: {
                            errorMessage.append("Invalid field: ").append(key).append("; ");
                            isValid = false;
                        }
                    }
                }

                context.disableDefaultConstraintViolation(); // Disable the default message
                context.buildConstraintViolationWithTemplate(errorMessage.toString())
                        .addConstraintViolation();
                return isValid;
            }
        }
    }
}
