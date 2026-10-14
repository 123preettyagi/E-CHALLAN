package REACT.REACT_CHALLAN.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import REACT.REACT_CHALLAN.entity.Grievance;

public interface GrievanceRepository extends JpaRepository<Grievance, Long> {

    Optional<Grievance> findByReferenceNumber(String referenceNumber);

    Page<Grievance> findByChallanNumber(String challanNumber, Pageable pageable);

    Page<Grievance> findByVehicleNumber(String vehicleNumber, Pageable pageable);

    Page<Grievance> findByMobile(String mobile, Pageable pageable);
}