package REACT.REACT_CHALLAN.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChallanRequestDTO {
    
    @NotBlank(message = "Vehicle number is required")
    @Size(max = 50, message = "Vehicle number must be less than 50 characters")
    private String vehicleNumber;
    
    @NotBlank(message = "Vehicle type is required")
    @Size(max = 50, message = "Vehicle type must be less than 50 characters")
    private String vehicleType;
    
    @NotBlank(message = "Owner name is required")
    @Size(max = 100, message = "Owner name must be less than 100 characters")
    private String ownerName;
    
    @NotBlank(message = "Registration state is required")
    @Size(max = 50, message = "Registration state must be less than 50 characters")
    private String registrationState;
    
    @NotNull(message = "Offence date is required")
    private LocalDate offenceDate;
    
    @NotNull(message = "Offence time is required")
    private LocalTime offenceTime;
    
    @NotBlank(message = "Offence place is required")
    @Size(max = 200, message = "Offence place must be less than 200 characters")
    private String offencePlace;
    
    @NotBlank(message = "Offence type is required")
    @Size(max = 100, message = "Offence type must be less than 100 characters")
    private String offenceType;
    
    private Double fineAmount;
    
    @NotNull(message = "Vehicle image is required")
    private MultipartFile vehicleImage;
    
    // Getters and Setters
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    public String getRegistrationState() {
        return registrationState;
    }
    
    public void setRegistrationState(String registrationState) {
        this.registrationState = registrationState;
    }
    
    public LocalDate getOffenceDate() {
        return offenceDate;
    }
    
    public void setOffenceDate(LocalDate offenceDate) {
        this.offenceDate = offenceDate;
    }
    
    
    
    
    public LocalTime getOffenceTime() {
		return offenceTime;
	}

	public void setOffenceTime(LocalTime offenceTime) {
		this.offenceTime = offenceTime;
	}

	public String getOffencePlace() {
        return offencePlace;
    }
    
    public void setOffencePlace(String offencePlace) {
        this.offencePlace = offencePlace;
    }
    
    public String getOffenceType() {
        return offenceType;
    }
    
    public void setOffenceType(String offenceType) {
        this.offenceType = offenceType;
    }
    
    public Double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public MultipartFile getVehicleImage() {
        return vehicleImage;
    }
    
    public void setVehicleImage(MultipartFile vehicleImage) {
        this.vehicleImage = vehicleImage;
    }
}