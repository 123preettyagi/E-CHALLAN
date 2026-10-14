package REACT.REACT_CHALLAN.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "challan_data")
public class Challan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @Column(name = "challan_number", unique = true, nullable = false)
    private String challanNumber;
    
    @Column(name = "vehicle_number", nullable = false, length = 50)
    private String vehicleNumber;
    
    @Column(name = "vehicle_type", nullable = false, length = 50)
    private String vehicleType;
    
    @Column(name = "owner_name", nullable = false, length = 100)
    private String ownerName;
    
    @Column(name = "registration_state", nullable = false, length = 50)
    private String registrationState;
    
    @Column(name = "offence_date", nullable = false)
    private LocalDate offenceDate;
    
    @Column(name = "offence_time", nullable = false)
    private LocalTime offenceTime;
    
    @Column(name = "offence_place", nullable = false, length = 200)
    private String offencePlace;
    
    @Column(name = "offence_type", nullable = false, length = 100)
    private String offenceType;
    
    @Column(name = "fine_amount")
    private Double fineAmount;
    
    @Lob
    @Column(name = "vehicle_image", nullable = false)
    private byte[] vehicleImage;
    
    @Column(name = "image_name", length = 255)
    private String imageName;
    
    @Column(name = "image_type", length = 50)
    private String imageType;
    
    
    // ===== NEW STATUS FIELD =====
    @Enumerated(EnumType.STRING)
    @Column(name = "challan_status", nullable = false)
   // @Column(name = "challan_status")
    private ChallanStatus challanStatus;
    
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    
    // Default constructor
    public Challan() {
        // Set default status when object is created
        this.challanStatus = ChallanStatus.UNPAID;
    }
    // Parameterized constructor
    public Challan(String challanNumber, String vehicleNumber, String vehicleType, String ownerName, 
                   String registrationState, LocalDate offenceDate, LocalTime offenceTime, String offencePlace,
                   String offenceType, Double fineAmount, byte[] vehicleImage,
                   String imageName, String imageType)
    {
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
        this.vehicleImage = vehicleImage;
        this.imageName = imageName;
        this.imageType = imageType;
     // Set default status
        this.challanStatus = ChallanStatus.UNPAID;
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
    
    public byte[] getVehicleImage() {
        return vehicleImage;
    }
    
    public void setVehicleImage(byte[] vehicleImage) {
        this.vehicleImage = vehicleImage;
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
    
    
    
    
    
    public ChallanStatus getChallanStatus() {
		return challanStatus;
	}
	public void setChallanStatus(ChallanStatus challanStatus) {
		this.challanStatus = challanStatus;
	}
	public void setImageType(String imageType) {
        this.imageType = imageType;
    }
    
    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PrePersist
    protected void onCreate() 
    {
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = java.time.LocalDateTime.now();
     // Ensure status is set if not already set
        if (this.challanStatus == null) {
            this.challanStatus = ChallanStatus.UNPAID;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = java.time.LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Challan{" +
                "id=" + id +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", registrationState='" + registrationState + '\'' +
                ", offenceDate=" + offenceDate +
                ", offenceTime=" + offenceTime +
                ", offencePlace='" + offencePlace + '\'' +
                ", offenceType='" + offenceType + '\'' +
                ", fineAmount=" + fineAmount +
                ", imageName='" + imageName + '\'' +
                ", imageType='" + imageType + '\'' +
                ", challanStatus=" + challanStatus +
                '}';
    }
}