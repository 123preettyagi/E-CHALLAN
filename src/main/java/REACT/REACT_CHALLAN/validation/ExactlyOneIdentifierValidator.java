package REACT.REACT_CHALLAN.validation;

import org.springframework.util.StringUtils;

import REACT.REACT_CHALLAN.dto.GrievanceRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExactlyOneIdentifierValidator implements ConstraintValidator<ExactlyOneIdentifier, GrievanceRequest>
{

    @Override
    public boolean isValid(GrievanceRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        boolean hasChallan = StringUtils.hasText(request.getChallanNumber());
        boolean hasVehicle = StringUtils.hasText(request.getVehicleNumber());

        boolean valid = hasChallan ^ hasVehicle; // exactly one must be true

        if (!valid) {
            context.disableDefaultConstraintViolation();
            String msg = (hasChallan && hasVehicle)
                    ? "Search by either challan number or vehicle number — not both."
                    : "Provide either a challan number or a vehicle number.";
            context.buildConstraintViolationWithTemplate(msg)
                    .addPropertyNode("challanNumber")
                    .addConstraintViolation();
        }

        return valid;
    }
}