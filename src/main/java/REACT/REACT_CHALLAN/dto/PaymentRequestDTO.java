package REACT.REACT_CHALLAN.dto;

import jakarta.validation.constraints.NotBlank;



public class PaymentRequestDTO {
    
    // Either challanNumber or vehicleNumber must be provided
    private String challanNumber;
    private String vehicleNumber;
    
    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // e.g., CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING
    
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private String upiId;
    private String transactionId;
    
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
    
   
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getCardHolderName() {
        return cardHolderName;
    }
    
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
    
    public String getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    
    public String getUpiId() {
        return upiId;
    }
    
    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}