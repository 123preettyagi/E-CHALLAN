package REACT.REACT_CHALLAN.entity;

public enum GrievanceCategory {
    INCORRECT_CHALLAN_ISSUED("Incorrect challan issued"),
    WRONG_VEHICLE_DETAILS("Wrong vehicle details"),
    DUPLICATE_CHALLAN("Duplicate challan"),
    PAYMENT_NOT_REFLECTED("Payment not reflected"),
    VEHICLE_ALREADY_SOLD_TRANSFERRED("Vehicle already sold/transferred"),
    DISPUTED_VIOLATION("Disputed violation"),
    OTHER("Other");

    private final String label;

    GrievanceCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}