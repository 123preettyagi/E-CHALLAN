package REACT.REACT_CHALLAN.dto;

import java.time.LocalDateTime;

import REACT.REACT_CHALLAN.entity.Grievance;
import REACT.REACT_CHALLAN.entity.GrievanceCategory;
import REACT.REACT_CHALLAN.entity.GrievanceStatus;

public class GrievanceResponse {

    private Long id;
    private String referenceNumber;
    private String challanNumber;
    private String vehicleNumber;
    private GrievanceCategory category;
    private String description;
    private String fullName;
    private String mobile;
    private String email;
    private GrievanceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GrievanceResponse from(Grievance g) {
        GrievanceResponse r = new GrievanceResponse();
        r.id = g.getId();
        r.referenceNumber = g.getReferenceNumber();
        r.challanNumber = g.getChallanNumber();
        r.vehicleNumber = g.getVehicleNumber();
        r.category = g.getCategory();
        r.description = g.getDescription();
        r.fullName = g.getFullName();
        r.mobile = g.getMobile();
        r.email = g.getEmail();
        r.status = g.getStatus();
        r.createdAt = g.getCreatedAt();
        r.updatedAt = g.getUpdatedAt();
        return r;
    }

    // ---- Getters and Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
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

    public GrievanceStatus getStatus() {
        return status;
    }

    public void setStatus(GrievanceStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}