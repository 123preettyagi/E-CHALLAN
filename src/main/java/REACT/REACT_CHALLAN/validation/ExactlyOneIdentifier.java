package REACT.REACT_CHALLAN.validation;

import java.lang.annotation.*;

import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExactlyOneIdentifierValidator.class)
@Documented
public @interface ExactlyOneIdentifier {

    String message() default "Provide either challanNumber or vehicleNumber — not both, and not neither.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}