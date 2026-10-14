package REACT.REACT_CHALLAN.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import REACT.REACT_CHALLAN.entity.ChallanStatus;

public class ChallanResponseDTO {
    private Long id;
    private String challanNumber;
    private String vehicleNumber;
    private String vehicleType;
    private String ownerName;
    private String registrationState;
    private LocalDate offenceDate;
    private LocalTime offenceTime;
    private String offencePlace;
    private String offenceType;
    private Double fineAmount;
    private String imageName;
    private String imageType;
    // ===== NEW STATUS FIELD =====
    private ChallanStatus challanStatus;
    private String message;
    private boolean success;
    
    // Default constructor
    public ChallanResponseDTO() {
    }
    
    // Parameterized constructor
    public ChallanResponseDTO(Long id,String challanNumber,String vehicleNumber, String vehicleType, 
                              String ownerName, String registrationState, 
                              LocalDate offenceDate, String offencePlace, 
                              LocalTime offenceTime,
                              String offenceType, Double fineAmount, 
                              String imageName, String imageType, 
                              ChallanStatus challanStatus,
                              String message, boolean success) {
        this.id = id;
        this.challanNumber=challanNumber;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.ownerName = ownerName;
        this.registrationState = registrationState;
        this.offenceDate = offenceDate;
        this.offenceTime=offenceTime;
        this.offencePlace = offencePlace;
        this.offenceType = offenceType;
        this.fineAmount = fineAmount;
        this.imageName = imageName;
        this.imageType = imageType;
        this.challanStatus = challanStatus;
        this.message = message;
        this.success = success;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    
    
    
    public String getChallanNumber() {
		return challanNumber;
	}

	public void setChallanNumber(String challanNumber) {
		this.challanNumber = challanNumber;
	}

	public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getImageName() {
        return imageName;
    }
    
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    
    public String getImageType() {
        return imageType;
    }
    
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
    
    
    
    
    public ChallanStatus getChallanStatus() {
		return challanStatus;
	}

	public void setChallanStatus(ChallanStatus challanStatus) {
		this.challanStatus = challanStatus;
	}

	public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}