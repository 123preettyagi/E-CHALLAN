package REACT.REACT_CHALLAN.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import REACT.REACT_CHALLAN.dto.PaymentRequestDTO;
import REACT.REACT_CHALLAN.dto.PaymentResponseDTO;
import REACT.REACT_CHALLAN.entity.Challan;
import REACT.REACT_CHALLAN.entity.ChallanStatus;
import REACT.REACT_CHALLAN.entity.Payment;
import REACT.REACT_CHALLAN.entity.PaymentStatus;
import REACT.REACT_CHALLAN.repository.ChallanRepository;
import REACT.REACT_CHALLAN.repository.PaymentRepository;

@Service
@Transactional
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private ChallanRepository challanRepository;
    
    @Autowired
    private ChallanService challanService;
    
    /**
     * Process payment for a challan
     * Either challanNumber or vehicleNumber must be provided
     * Amount is automatically fetched from challan table
     */
    
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) 
    {
        
        // Validate input
        if ((request.getChallanNumber() == null || request.getChallanNumber().trim().isEmpty()) &&
            (request.getVehicleNumber() == null || request.getVehicleNumber().trim().isEmpty())) {
            return new PaymentResponseDTO(false, "Either Challan Number or Vehicle Number is required", 
                                        null, null, null, null, null, null, null, null);
        }
        
        // Find the challan(s) to be paid
        List<Challan> challansToPay;
        
        if (request.getChallanNumber() != null && !request.getChallanNumber().trim().isEmpty()) {
            // Search by Challan Number
            Optional<Challan> challanOpt = challanRepository.findByChallanNumber(request.getChallanNumber().trim());
            if (challanOpt.isPresent()) {
                challansToPay = List.of(challanOpt.get());
            } else {
                return new PaymentResponseDTO(false, "Challan not found with number: " + request.getChallanNumber(), 
                                            null, request.getChallanNumber(), null, null, null, null, null, null);
            }
        } else {
            // Search by Vehicle Number
            challansToPay = challanRepository.findByVehicleNumber(request.getVehicleNumber().trim());
            if (challansToPay.isEmpty()) {
                return new PaymentResponseDTO(false, "No challans found for vehicle: " + request.getVehicleNumber(), 
                                            null, null, request.getVehicleNumber(), null, null, null, null, null);
            }
        }
        
        // Filter only UNPAID challans
        List<Challan> unpaidChallans = challansToPay.stream()
            .filter(c -> c.getChallanStatus() == ChallanStatus.UNPAID)
            .toList();
        
        if (unpaidChallans.isEmpty()) {
            return new PaymentResponseDTO(false, "All challans are already paid or not eligible for payment", 
                                        null, null, request.getVehicleNumber(), null, null, null, null, null);
        }
        
        // Calculate total amount from challan(s) - AUTOMATICALLY FETCHED
        Double totalAmount = unpaidChallans.stream()
            .mapToDouble(Challan::getFineAmount)
            .sum();
        
        // Process payment
        PaymentResponseDTO paymentResult = processPaymentGateway(request, unpaidChallans, totalAmount);
        
        if (paymentResult.isSuccess()) {
            // Update challan status to PAID
            for (Challan challan : unpaidChallans) {
                challan.setChallanStatus(ChallanStatus.PAID);
                challan.setUpdatedAt(LocalDateTime.now());
                challanRepository.save(challan);
            }
            
            // Save payment record
            Payment payment = new Payment(
                paymentResult.getTransactionId(),
                unpaidChallans.get(0).getChallanNumber(),
                unpaidChallans.get(0).getVehicleNumber(),
                totalAmount,
                request.getPaymentMethod(),
                PaymentStatus.COMPLETED
            );
            payment.setReferenceNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            paymentRepository.save(payment);
            
            // Build response
            paymentResult.setChallanNumber(unpaidChallans.size() == 1 ? 
                unpaidChallans.get(0).getChallanNumber() : 
                "Multiple challans paid");
            paymentResult.setVehicleNumber(unpaidChallans.get(0).getVehicleNumber());
            paymentResult.setAmountPaid(totalAmount);
            paymentResult.setNewStatus(ChallanStatus.PAID);
            paymentResult.setPaymentDate(LocalDateTime.now());
            
            return paymentResult;
        }
        
        return paymentResult;
    } 
    
    
    
    
    /**
     * Simulate payment gateway processing
     * 
     */
    
    private PaymentResponseDTO processPaymentGateway(PaymentRequestDTO request, 
                                                     List<Challan> challans, 
                                                     Double totalAmount) 
    {
        // Generate unique transaction ID
        String transactionId = "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6);
        
        // Simulate payment success/failure (95% success rate)
        boolean paymentSuccess = Math.random() < 0.95;
        
        if (paymentSuccess) {
            return new PaymentResponseDTO(
                true,
                "Payment processed successfully for " + challans.size() + " challan(s)",
                transactionId,
                null,
                null,
                totalAmount,
                request.getPaymentMethod(),
                ChallanStatus.PAID,
                LocalDateTime.now(),
                "/api/challan/download?challanNumber=" + challans.get(0).getChallanNumber()
            );
        } else {
            return new PaymentResponseDTO(
                false,
                "Payment failed. Please try again.",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            );
        }
    }  
    
}