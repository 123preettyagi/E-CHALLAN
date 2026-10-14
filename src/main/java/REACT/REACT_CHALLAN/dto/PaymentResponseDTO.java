package REACT.REACT_CHALLAN.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import REACT.REACT_CHALLAN.entity.ChallanStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponseDTO {
    private boolean success;
    private String message;
    private String transactionId;
    private String challanNumber;
    private String vehicleNumber;
    private Double amountPaid;
    private String paymentMethod;
    private ChallanStatus newStatus;
    private LocalDateTime paymentDate;
    private String receiptUrl;
    
    // Default constructor
    public PaymentResponseDTO() {}
    
    // Parameterized constructor
    public PaymentResponseDTO(boolean success, String message, String transactionId, 
                              String challanNumber, String vehicleNumber, Double amountPaid,
                              String paymentMethod, ChallanStatus newStatus, 
                              LocalDateTime paymentDate, String receiptUrl) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
        this.challanNumber = challanNumber;
        this.vehicleNumber = vehicleNumber;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.newStatus = newStatus;
        this.paymentDate = paymentDate;
        this.receiptUrl = receiptUrl;
    }
    
    // Getters and Setters
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
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
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
    
    public Double getAmountPaid() {
        return amountPaid;
    }
    
    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public ChallanStatus getNewStatus() {
        return newStatus;
    }
    
    public void setNewStatus(ChallanStatus newStatus) {
        this.newStatus = newStatus;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getReceiptUrl() {
        return receiptUrl;
    }
    
    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }
}