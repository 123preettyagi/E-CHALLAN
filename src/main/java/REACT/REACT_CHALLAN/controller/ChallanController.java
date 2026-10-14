package REACT.REACT_CHALLAN.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import REACT.REACT_CHALLAN.dto.ChallanRequestDTO;
import REACT.REACT_CHALLAN.dto.ChallanResponseDTO;
import REACT.REACT_CHALLAN.dto.ChallanStatusResponseDTO;
import REACT.REACT_CHALLAN.dto.GrievanceRequest;
import REACT.REACT_CHALLAN.dto.GrievanceResponse;
import REACT.REACT_CHALLAN.dto.PaymentRequestDTO;
import REACT.REACT_CHALLAN.dto.PaymentResponseDTO;
import REACT.REACT_CHALLAN.dto.StatusUpdateRequest;
import REACT.REACT_CHALLAN.service.ChallanService;
import REACT.REACT_CHALLAN.service.GrievanceService;
import REACT.REACT_CHALLAN.service.PaymentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/challan")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class ChallanController {
    
    @Autowired
    private ChallanService challanService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private GrievanceService grievanceService;
    
    @PostMapping(value="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChallanResponseDTO> createChallan(
            @RequestHeader("Authorization") String authorization,
            @Valid @ModelAttribute ChallanRequestDTO challanRequestDTO)
            throws IOException 
    {
        ChallanResponseDTO response = challanService.createChallan(challanRequestDTO, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<ChallanResponseDTO>> getAllChallans() {
        List<ChallanResponseDTO> challans = challanService.getAllChallans();
        return new ResponseEntity<>(challans, HttpStatus.OK);
    }
    
    @GetMapping("/vehicle/{vehicleNumber}")
    public ResponseEntity<List<ChallanResponseDTO>> getChallansByVehicleNumber(
            @PathVariable String vehicleNumber) {
        List<ChallanResponseDTO> challans = challanService.getChallansByVehicleNumber(vehicleNumber);
        return new ResponseEntity<>(challans, HttpStatus.OK);
    }
    
    @GetMapping("/{challanNumber}")
    public ResponseEntity<ChallanResponseDTO> getChallanByChallanNumber(
            @PathVariable String challanNumber) {
        ChallanResponseDTO response = challanService.getChallanByChallanNumber(challanNumber);
        
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // DOWNLOAD RECEIPT API
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadChallanReceipt(
            @RequestParam(value = "challanNumber", required = false) String challanNumber,
            @RequestParam(value = "vehicleNo", required = false) String vehicleNumber) 
    {
        
        System.out.println("Vehicle Number = " + vehicleNumber);
        System.out.println("Challan Number = " + challanNumber);
        
        // Validate: At least one parameter is required
        if ((challanNumber == null || challanNumber.trim().isEmpty()) && 
            (vehicleNumber == null || vehicleNumber.trim().isEmpty())) 
        {
            return ResponseEntity
                    .badRequest()
                    .body("Please provide either Challan Number or Vehicle Number".getBytes());
        }
        
        try 
        {
            byte[] pdfContent = challanService.downloadChallanReceipt(challanNumber, vehicleNumber);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "challan_receipt.pdf");
            
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(pdfContent);
                    
        } 
        catch (IllegalArgumentException e) 
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage().getBytes());
        } 
        catch (Exception e) 
        {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
                        
    }
    
    
// ==================== CHALLAN PAYMENT ENDPOINTS ====================
    
  
    @PostMapping("/payment/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @Valid @RequestBody PaymentRequestDTO request) {
        
        PaymentResponseDTO response = paymentService.processPayment(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    
    
    
   
 // ==================== CHALLAN STATUS  ENDPOINTS ====================
    @GetMapping("/status/{challanNumber}")
    public ResponseEntity<ChallanStatusResponseDTO> getChallanStatus(
            @PathVariable String challanNumber) {
        
        ChallanStatusResponseDTO response = challanService.getChallanStatus(challanNumber);
        
        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/status/vehicle/{vehicleNumber}")
    public ResponseEntity<List<ChallanStatusResponseDTO>> getChallanStatusByVehicleNumber(
            @PathVariable String vehicleNumber) {
        
        List<ChallanStatusResponseDTO> responses = 
            challanService.getChallanStatusByVehicleNumber(vehicleNumber);
        
        if (responses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responses);
        }
        
        return ResponseEntity.ok(responses);
    }
  
    
    
    // ================================================================
    // ==================== GRIEVANCE ENDPOINTS ====================
    // Base path: /api/grievances
    // ================================================================

    @PostMapping("/grievances")
    public ResponseEntity<GrievanceResponse> submitGrievance(
            @Valid @RequestBody GrievanceRequest request) {
        GrievanceResponse response = grievanceService.submit(request);
        return ResponseEntity.created(URI.create("/api/grievances/" + response.getId()))
                .body(response);
    }

    @GetMapping("/grievances/{id}")
    public ResponseEntity<GrievanceResponse> getGrievanceById(@PathVariable Long id) {
        return ResponseEntity.ok(grievanceService.getById(id));
    }

    @GetMapping("/grievances/reference/{referenceNumber}")
    public ResponseEntity<GrievanceResponse> getGrievanceByReference(
            @PathVariable String referenceNumber) {
        return ResponseEntity.ok(grievanceService.getByReferenceNumber(referenceNumber));
    }

    @GetMapping("/grievances")
    public ResponseEntity<Page<GrievanceResponse>> listGrievances(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(grievanceService.list(pageable));
    }

    @GetMapping("/grievances/search/challan/{challanNumber}")
    public ResponseEntity<Page<GrievanceResponse>> searchGrievancesByChallan(
            @PathVariable String challanNumber,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(grievanceService.searchByChallan(challanNumber, pageable));
    }

    @GetMapping("/grievances/search/vehicle/{vehicleNumber}")
    public ResponseEntity<Page<GrievanceResponse>> searchGrievancesByVehicle(
            @PathVariable String vehicleNumber,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(grievanceService.searchByVehicle(vehicleNumber, pageable));
    }

    @PatchMapping("/grievances/{id}/status")
    public ResponseEntity<GrievanceResponse> updateGrievanceStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(grievanceService.updateStatus(id, request));
    }

    @DeleteMapping("/grievances/{id}")
    public ResponseEntity<Void> deleteGrievance(@PathVariable Long id) {
        grievanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
    