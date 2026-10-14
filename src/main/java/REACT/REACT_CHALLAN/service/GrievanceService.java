package REACT.REACT_CHALLAN.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import REACT.REACT_CHALLAN.dto.GrievanceRequest;
import REACT.REACT_CHALLAN.dto.GrievanceResponse;
import REACT.REACT_CHALLAN.dto.StatusUpdateRequest;
import REACT.REACT_CHALLAN.entity.Grievance;
import REACT.REACT_CHALLAN.repository.ChallanRepository;
import REACT.REACT_CHALLAN.repository.GrievanceRepository;
import REACT.REACT_CHALLAN.util.ResourceNotFoundException;
import jakarta.validation.ValidationException;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GrievanceService {

    private final GrievanceRepository repository;
    private final ChallanRepository challanRepository;

    // simple in-memory sequence for human-friendly reference numbers
    private final AtomicLong sequence = new AtomicLong(1);

    public GrievanceService(GrievanceRepository repository,ChallanRepository challanRepository)
    {
        this.repository = repository;
        this.challanRepository = challanRepository;
    }

    public GrievanceResponse submit(GrievanceRequest request) 
    {
    	 // Validate that either challanNumber or vehicleNumber exists in Challan table
        validateChallanOrVehicleExists(request);
    	
        Grievance g = new Grievance();
        g.setChallanNumber(request.getChallanNumber());
        g.setVehicleNumber(request.getVehicleNumber());
        g.setCategory(request.getCategory());
        g.setDescription(request.getDescription());
        g.setFullName(request.getFullName());
        g.setMobile(request.getMobile());
        g.setEmail(request.getEmail());
        g.setReferenceNumber(generateReferenceNumber());

        Grievance saved = repository.save(g);
        return GrievanceResponse.from(saved);
    }
    
    
    private void validateChallanOrVehicleExists(GrievanceRequest request) {
        String challanNumber = request.getChallanNumber();
        String vehicleNumber = request.getVehicleNumber();
        
        // Check if at least one is provided
        if ((challanNumber == null || challanNumber.trim().isEmpty()) && 
            (vehicleNumber == null || vehicleNumber.trim().isEmpty())) {
            throw new ValidationException("Either Challan Number or Vehicle Number must be provided");
        }
        
        // Check if challanNumber exists (if provided)
        if (challanNumber != null && !challanNumber.trim().isEmpty()) {
            boolean challanExists = challanRepository.existsByChallanNumber(challanNumber);
            if (!challanExists) {
                throw new ValidationException("Challan number '" + challanNumber + "' does not exist in the system");
            }
        }
        
        // Check if vehicleNumber exists (if provided)
        if (vehicleNumber != null && !vehicleNumber.trim().isEmpty()) {
            boolean vehicleExists = challanRepository.existsByVehicleNumber(vehicleNumber);
            if (!vehicleExists) {
                throw new ValidationException("Vehicle number '" + vehicleNumber + "' does not exist in the system");
            }
        }
    }

    
    

    @Transactional(readOnly = true)
    public GrievanceResponse getById(Long id) {
        Grievance g = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grievance not found with id: " + id));
        return GrievanceResponse.from(g);
    }

    @Transactional(readOnly = true)
    public GrievanceResponse getByReferenceNumber(String referenceNumber) {
        Grievance g = repository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Grievance not found with reference: " + referenceNumber));
        return GrievanceResponse.from(g);
    }

    @Transactional(readOnly = true)
    public Page<GrievanceResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(GrievanceResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<GrievanceResponse> searchByChallan(String challanNumber, Pageable pageable) {
        return repository.findByChallanNumber(challanNumber, pageable).map(GrievanceResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<GrievanceResponse> searchByVehicle(String vehicleNumber, Pageable pageable) {
        return repository.findByVehicleNumber(vehicleNumber, pageable).map(GrievanceResponse::from);
    }

    public GrievanceResponse updateStatus(Long id, StatusUpdateRequest request) {
        Grievance g = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grievance not found with id: " + id));
        g.setStatus(request.getStatus());
        return GrievanceResponse.from(repository.save(g));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Grievance not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private String generateReferenceNumber() {
        return String.format("GRV-%06d", sequence.getAndIncrement());
    }
}