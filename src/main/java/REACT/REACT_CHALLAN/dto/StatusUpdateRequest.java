package REACT.REACT_CHALLAN.dto;

import REACT.REACT_CHALLAN.entity.GrievanceStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {

    @NotNull(message = "status is required")
    private GrievanceStatus status;

    public GrievanceStatus getStatus() {
        return status;
    }

    public void setStatus(GrievanceStatus status) {
        this.status = status;
    }
}