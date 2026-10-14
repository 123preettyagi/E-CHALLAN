package REACT.REACT_CHALLAN.dto;

public class ChallanStatusResponseDTO {
    private String challanNumber;
    private String vehicleNumber;
    private String status;
    private Double fineAmount;
    private String ownerName;
    private String offenceType;
    private String offencePlace;
    private boolean success;
    private String message;
    
    // Constructors
    public ChallanStatusResponseDTO() {}
    
    public ChallanStatusResponseDTO(String challanNumber, String vehicleNumber, 
                                    String status, Double fineAmount, 
                                    String ownerName, String offenceType, 
                                    String offencePlace) {
        this.challanNumber = challanNumber;
        this.vehicleNumber = vehicleNumber;
        this.status = status;
        this.fineAmount = fineAmount;
        this.ownerName = ownerName;
        this.offenceType = offenceType;
        this.offencePlace = offencePlace;
    }
    
    // Getters and Setters
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    public String getOffenceType() {
        return offenceType;
    }
    
    public void setOffenceType(String offenceType) {
        this.offenceType = offenceType;
    }
    
    public String getOffencePlace() {
        return offencePlace;
    }
    
    public void setOffencePlace(String offencePlace) {
        this.offencePlace = offencePlace;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}