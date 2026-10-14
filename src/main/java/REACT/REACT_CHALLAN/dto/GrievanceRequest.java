package REACT.REACT_CHALLAN.dto;

import REACT.REACT_CHALLAN.entity.GrievanceCategory;
import REACT.REACT_CHALLAN.validation.ExactlyOneIdentifier;
import jakarta.validation.constraints.*;

@ExactlyOneIdentifier
public class GrievanceRequest {

    @Pattern(regexp = "^[A-Z]{2}[0-9]{14}$", message = "Challan number must look like CH20260819681734")
    private String challanNumber;

    @Pattern(regexp = "^[A-Z]{2}-[A-Z]{2}-[0-9]{4}-[0-9]{2}$", message = "Enter a valid vehicle registration number, e.g. KA-BA-9399-23")
    private String vehicleNumber;

    @NotNull(message = "Grievance category is required")
    private GrievanceCategory category;

    @NotBlank(message = "Please describe the issue")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number must be a valid 10-digit Indian number")
    private String mobile;

    @Email(message = "Enter a valid email address")
    private String email;

    public String getChallanNumber() {
        return challanNumber;
    }

    public void setChallanNumber(String challanNumber) {
        this.challanNumber = challanNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public GrievanceCategory getCategory() {
        return category;
    }

    public void setCategory(GrievanceCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}