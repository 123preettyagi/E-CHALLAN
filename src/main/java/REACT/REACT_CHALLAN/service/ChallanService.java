package REACT.REACT_CHALLAN.service;

import java.io.IOException;
import java.util.List;

import REACT.REACT_CHALLAN.dto.ChallanRequestDTO;
import REACT.REACT_CHALLAN.dto.ChallanResponseDTO;
import REACT.REACT_CHALLAN.dto.ChallanStatusResponseDTO;
import REACT.REACT_CHALLAN.entity.Challan;

public interface ChallanService 
{
    ChallanResponseDTO createChallan(ChallanRequestDTO challanRequestDTO,String authorization) throws IOException;
    ChallanResponseDTO getChallanById(Long id);
    List<ChallanResponseDTO> getAllChallans();
    List<ChallanResponseDTO> getChallansByVehicleNumber(String vehicleNumber);
    ChallanResponseDTO convertToDTO(Challan challan);
    
    // Add this new method
    ChallanResponseDTO getChallanByChallanNumber(String challanNumber);
    
    // ==================== NEW METHOD ====================
    byte[] downloadChallanReceipt(String challanNumber, String vehicleNumber) throws Exception;
    
    // Challan status
    ChallanStatusResponseDTO getChallanStatus(String challanNumber);
    
 // Add to ChallanService interface
    List<ChallanStatusResponseDTO> getChallanStatusByVehicleNumber(String vehicleNumber);
    
}