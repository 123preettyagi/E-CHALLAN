package REACT.REACT_CHALLAN.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import REACT.REACT_CHALLAN.entity.Challan;
import REACT.REACT_CHALLAN.entity.ChallanStatus;

@Repository
public interface ChallanRepository extends JpaRepository<Challan, Long> 
{
    List<Challan> findByVehicleNumber(String vehicleNumber);
    List<Challan> findByOwnerNameContainingIgnoreCase(String ownerName);
    
    boolean existsByChallanNumber(String challanNumber);
    
    
    Optional<Challan> findTopByVehicleNumberAndOffencePlaceOrderByCreatedAtDesc(
            String vehicleNumber,
            String offencePlace
    );
    
    
    boolean existsByVehicleNumber(String vehicleNumber);
    
 // Add this method
    Optional<Challan> findByChallanNumber(String challanNumber);
    
    
// NEW METHODS FOR PAYMENT
    
    @Modifying
    @Query("UPDATE Challan c SET c.challanStatus = :status, c.updatedAt = :updatedAt WHERE c.challanNumber = :challanNumber")
    int updateChallanStatus(@Param("challanNumber") String challanNumber, 
                            @Param("status") ChallanStatus status,
                            @Param("updatedAt") LocalDateTime updatedAt);
    
    Optional<Challan> findByChallanNumberAndChallanStatus(String challanNumber, ChallanStatus status);
    
    List<Challan> findByVehicleNumberAndChallanStatus(String vehicleNumber, ChallanStatus status);
    
    @Modifying
    @Query("UPDATE Challan c SET c.challanStatus = :status WHERE c.challanNumber IN :challanNumbers")
    int updateChallanStatusByChallanNumbers(@Param("challanNumbers") List<String> challanNumbers, 
                                            @Param("status") ChallanStatus status);

    
}