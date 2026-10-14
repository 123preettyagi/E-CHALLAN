package REACT.REACT_CHALLAN.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import REACT.REACT_CHALLAN.entity.Payment;
import REACT.REACT_CHALLAN.entity.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> 
{
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    List<Payment> findByChallanNumber(String challanNumber);
    
    List<Payment> findByVehicleNumber(String vehicleNumber);
    
    List<Payment> findByPaymentStatus(String paymentStatus);
    
    boolean existsByTransactionId(String transactionId);
    
}