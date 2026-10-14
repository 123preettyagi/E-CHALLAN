package REACT.REACT_CHALLAN.service;

import java.io.ByteArrayOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import REACT.REACT_CHALLAN.dto.ChallanRequestDTO;
import REACT.REACT_CHALLAN.dto.ChallanResponseDTO;
import REACT.REACT_CHALLAN.dto.ChallanStatusResponseDTO;
import REACT.REACT_CHALLAN.entity.Challan;
import REACT.REACT_CHALLAN.repository.ChallanRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ChallanServiceImpl implements ChallanService {
    
    @Autowired
    private ChallanRepository challanRepository;
    
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Override
    public ChallanResponseDTO createChallan(ChallanRequestDTO challanRequestDTO,String authorization) throws IOException 
    {
    	
    	
    	 // Vehicle Number Validation
        String vehicleNumber = challanRequestDTO.getVehicleNumber();

        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle number is required");
        }

        // Format: DL-RT-7924-23
        String vehiclePattern = "^[A-Z]{2}-[A-Z]{2}-\\d{4}-\\d{2}$";

        if (!vehicleNumber.matches(vehiclePattern)) {
            throw new IllegalArgumentException(
                "Invalid vehicle number. Format should be like DL-RT-7924-23");
        }

    	
        // Validate image
        MultipartFile imageFile = challanRequestDTO.getVehicleImage();
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Vehicle image is required");
        }
        
        // Validate file size (max 10MB)
        if (imageFile.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Image size should be less than 10MB");
        }
        
        // Validate file type
        String contentType = imageFile.getContentType();
        if (contentType == null || !(contentType.equals("image/png") || 
                                     contentType.equals("image/jpeg") || 
                                     contentType.equals("image/gif"))) {
            throw new IllegalArgumentException("Only PNG, JPG, and GIF images are allowed");
        }
        
        
        Optional<Challan> previousChallan =
                challanRepository.findTopByVehicleNumberAndOffencePlaceOrderByCreatedAtDesc(
                        challanRequestDTO.getVehicleNumber(),
                        challanRequestDTO.getOffencePlace());

    
        
        if (previousChallan.isPresent()) {

            LocalDateTime lastCreated = previousChallan.get().getCreatedAt();

            LocalDateTime allowedTime = lastCreated.plusHours(24);

            if (LocalDateTime.now().isBefore(allowedTime)) {

                long remainingHours =
                        Duration.between(LocalDateTime.now(), allowedTime).toHours();

                long remainingMinutes =
                        Duration.between(LocalDateTime.now(), allowedTime).toMinutes() % 60;

                throw new IllegalArgumentException(
                        "A challan has already been issued for this vehicle at "
                                + challanRequestDTO.getOffencePlace()
                                + ". Another challan can be created after "
                                + remainingHours + " hours "
                                + remainingMinutes + " minutes.");
            }
        }
        // THEY CALL VAHAN API TO VERIFY THE VEHICAL NUMBER AND VEHICLE TYPE
        String url =
        	    "http://localhost:8080/api/vehicle/validate?vehicleNumber="
        	    + challanRequestDTO.getVehicleNumber()
        	    + "&vehicleType="
        	    + challanRequestDTO.getVehicleType();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        
     // ADD THESE LINES HERE
        System.out.println("=================================");
        System.out.println("URL = " + url);
        System.out.println("Authorization = " + authorization);
        System.out.println("=================================");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        	try {

        	    ResponseEntity<String> response =
        	            restTemplate.exchange(
        	            		url,
        	                    HttpMethod.GET,
        	                    entity,
        	                    String.class
        	            		);

        	    if (!response.getStatusCode().is2xxSuccessful()) {
        	        throw new IllegalArgumentException(
        	                "Vehicle not found in VAHAN database.");
        	    }

        	} 
        	catch (Exception e) {

        	    e.printStackTrace();   // Print actual error

        	    throw new IllegalArgumentException(
        	            "VAHAN API Error : " + e.getMessage());
        	}
        
        
        
        // Create Challan entity
        Challan challan = new Challan();
        
     // Generate Unique Challan Number
        challan.setChallanNumber(generateChallanNumber());
        
        challan.setVehicleNumber(challanRequestDTO.getVehicleNumber());
        challan.setVehicleType(challanRequestDTO.getVehicleType());
        challan.setOwnerName(challanRequestDTO.getOwnerName());
        challan.setRegistrationState(challanRequestDTO.getRegistrationState());
        challan.setOffenceDate(challanRequestDTO.getOffenceDate());
        challan.setOffenceTime(challanRequestDTO.getOffenceTime());
        challan.setOffencePlace(challanRequestDTO.getOffencePlace());
        challan.setOffenceType(challanRequestDTO.getOffenceType());
        challan.setFineAmount(challanRequestDTO.getFineAmount());
        
        // Store image as byte array
        challan.setVehicleImage(imageFile.getBytes());
        challan.setImageName(imageFile.getOriginalFilename());
        challan.setImageType(imageFile.getContentType());
        
        // Save to database
        Challan savedChallan = challanRepository.save(challan);
        
        // Return response
        ChallanResponseDTO response = convertToDTO(savedChallan);
        response.setMessage("Challan created successfully");
        response.setSuccess(true);
        
        return response;
    }
    
    private String generateChallanNumber() {

        String challanNumber;

        do {
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            int random = 100000 + new Random().nextInt(900000);

            // Example: CH20260730123456
            challanNumber = "CH" + date + random;

        } while (challanRepository.existsByChallanNumber(challanNumber));

        return challanNumber;
    }
    
    @Override
    public ChallanResponseDTO getChallanById(Long id) {
        Optional<Challan> challanOptional = challanRepository.findById(id);
        if (challanOptional.isPresent()) {
            ChallanResponseDTO response = convertToDTO(challanOptional.get());
            response.setMessage("Challan found");
            response.setSuccess(true);
            return response;
        } else {
            ChallanResponseDTO response = new ChallanResponseDTO();
            response.setMessage("Challan not found with id: " + id);
            response.setSuccess(false);
            return response;
        }
    }
    
    @Override
    public List<ChallanResponseDTO> getAllChallans() {
        List<Challan> challans = challanRepository.findAll();
        List<ChallanResponseDTO> responseDTOs = new ArrayList<>();
        for (Challan challan : challans) {
            responseDTOs.add(convertToDTO(challan));
        }
        return responseDTOs;
    }
    
    @Override
    public List<ChallanResponseDTO> getChallansByVehicleNumber(String vehicleNumber) {
        List<Challan> challans = challanRepository.findByVehicleNumber(vehicleNumber);
        List<ChallanResponseDTO> responseDTOs = new ArrayList<>();
        for (Challan challan : challans) {
            responseDTOs.add(convertToDTO(challan));
        }
        return responseDTOs;
    }
    
    @Override
    public ChallanResponseDTO convertToDTO(Challan challan)
    {
        ChallanResponseDTO dto = new ChallanResponseDTO();
        
        
        dto.setId(challan.getId());
        dto.setChallanNumber(challan.getChallanNumber());
        dto.setVehicleNumber(challan.getVehicleNumber());
        dto.setVehicleType(challan.getVehicleType());
        dto.setOwnerName(challan.getOwnerName());
        dto.setRegistrationState(challan.getRegistrationState());
        dto.setOffenceDate(challan.getOffenceDate());
        dto.setOffenceTime(challan.getOffenceTime());
        dto.setOffencePlace(challan.getOffencePlace());
        dto.setOffenceType(challan.getOffenceType());
        dto.setFineAmount(challan.getFineAmount());
        dto.setImageName(challan.getImageName());
        dto.setImageType(challan.getImageType());
     // ===== SET STATUS =====
        dto.setChallanStatus(challan.getChallanStatus());
        dto.setSuccess(true);
        return dto;
    }
    
    
    @Override
    public ChallanResponseDTO getChallanByChallanNumber(String challanNumber) 
    {
        // Validate input
        if (challanNumber == null || challanNumber.trim().isEmpty()) {
            ChallanResponseDTO response = new ChallanResponseDTO();
            response.setMessage("Challan number is required");
            response.setSuccess(false);
            return response;
        }
        
        // Find challan by challan number
        Optional<Challan> challanOptional = challanRepository.findByChallanNumber(challanNumber);
        
        if (challanOptional.isPresent()) {
            ChallanResponseDTO response = convertToDTO(challanOptional.get());
            response.setMessage("Challan found successfully");
            response.setSuccess(true);
            return response;
        } else {
            ChallanResponseDTO response = new ChallanResponseDTO();
            response.setMessage("Challan not found with challan number: " + challanNumber);
            response.setSuccess(false);
            return response;
        }
    }
    
    
 // ==================== CHALLAN PDF  DOWNLOAD METHOD ====================
   
    
    @Override
    public byte[] downloadChallanReceipt(String challanNumber, String vehicleNumber) throws Exception {
        
        List<Challan> challans = new ArrayList<>();
        String searchCriteria = "";
        
        // Search by Challan Number
        if (challanNumber != null && !challanNumber.trim().isEmpty()) {
            Optional<Challan> challanOpt = challanRepository.findByChallanNumber(challanNumber.trim());
            if (challanOpt.isPresent()) {
                challans.add(challanOpt.get());
                searchCriteria = "Challan Number: " + challanNumber;
            } else {
                throw new IllegalArgumentException("No challan found with Challan Number: " + challanNumber);
            }
        } 
        // Search by Vehicle Number
        else if (vehicleNumber != null && !vehicleNumber.trim().isEmpty()) {
            challans = challanRepository.findByVehicleNumber(vehicleNumber.trim());
            searchCriteria = "Vehicle Number: " + vehicleNumber;
            if (challans.isEmpty()) {
                throw new IllegalArgumentException("No challans found with Vehicle Number: " + vehicleNumber);
            }
        }
        
        // Generate PDF
        return generateChallanReceipt(challans, searchCriteria);
    }
    
    /**
     * Generate Professional Challan Receipt PDF
     */
    private byte[] generateChallanReceipt(List<Challan> challans, String searchCriteria) throws Exception {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        
        // ======= FONTS =======
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
        Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.DARK_GRAY);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new BaseColor(0, 51, 102));
        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY);
        
        // ======= HEADER SECTION =======
        // Top border line
        PdfPTable topLine = new PdfPTable(1);
        topLine.setWidthPercentage(100);
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorder(Rectangle.BOTTOM);
        lineCell.setBorderColor(new BaseColor(0, 51, 102));
        lineCell.setBorderWidth(3);
        lineCell.setPadding(0);
        lineCell.setFixedHeight(5);
        topLine.addCell(lineCell);
        document.add(topLine);
        
        document.add(new Paragraph("\n"));
        
        // Main Header
        Paragraph header = new Paragraph("TRAFFIC CHALLAN", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(5);
        document.add(header);
        
        // Sub-header
        Paragraph subHeader = new Paragraph("Government of India - Traffic Police Department", subHeaderFont);
        subHeader.setAlignment(Element.ALIGN_CENTER);
        subHeader.setSpacingAfter(10);
        document.add(subHeader);
        
        // Search Criteria
        Paragraph searchInfo = new Paragraph("Search Criteria: " + searchCriteria, FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY));
        searchInfo.setAlignment(Element.ALIGN_CENTER);
        searchInfo.setSpacingAfter(15);
        document.add(searchInfo);
        
        // ======= GENERATE RECEIPT(S) =======
        for (int i = 0; i < challans.size(); i++) {
            Challan challan = challans.get(i);
            
            if (i > 0) {
                document.add(new Paragraph("\n"));
                document.add(new Paragraph("--------------------------------------------------"));
                document.add(new Paragraph("\n"));
            }
            
            // ======= CHALLAN NUMBER SECTION =======
            PdfPTable challanNumberTable = new PdfPTable(1);
            challanNumberTable.setWidthPercentage(100);
            challanNumberTable.setSpacingAfter(10);
            
            PdfPCell numberCell = new PdfPCell(new Phrase("CHALLAN NO: " + challan.getChallanNumber(), titleFont));
            numberCell.setBackgroundColor(new BaseColor(240, 244, 250));
            numberCell.setPadding(10);
            numberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            numberCell.setBorder(Rectangle.BOX);
            numberCell.setBorderColor(new BaseColor(0, 51, 102));
            numberCell.setBorderWidth(1);
            challanNumberTable.addCell(numberCell);
            document.add(challanNumberTable);
            
            document.add(new Paragraph("\n"));
            
            // ======= MAIN TABLE =======
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(5);
            table.setSpacingAfter(10);
            
            // Column widths
            float[] columnWidths = {0.35f, 0.65f};
            table.setWidths(columnWidths);
            
            // ======= VEHICLE DETAILS SECTION =======
            // Section Header
            PdfPCell vehicleHeader = new PdfPCell(new Phrase("VEHICLE DETAILS", labelFont));
            vehicleHeader.setColspan(2);
            vehicleHeader.setBackgroundColor(new BaseColor(220, 235, 255));
            vehicleHeader.setPadding(8);
            vehicleHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            vehicleHeader.setBorder(Rectangle.BOX);
            table.addCell(vehicleHeader);
            
            // Vehicle Details
            addStyledTableRow(table, "Vehicle Number:", challan.getVehicleNumber(), labelFont, valueFont);
            addStyledTableRow(table, "Vehicle Type:", challan.getVehicleType(), labelFont, valueFont);
            addStyledTableRow(table, "Registration State:", challan.getRegistrationState(), labelFont, valueFont);
            
            // ======= OWNER DETAILS =======
            PdfPCell ownerHeader = new PdfPCell(new Phrase("OWNER DETAILS", labelFont));
            ownerHeader.setColspan(2);
            ownerHeader.setBackgroundColor(new BaseColor(220, 235, 255));
            ownerHeader.setPadding(8);
            ownerHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            ownerHeader.setBorder(Rectangle.BOX);
            table.addCell(ownerHeader);
            
            addStyledTableRow(table, "Owner Name:", challan.getOwnerName(), labelFont, valueFont);
            
            // ======= OFFENCE DETAILS =======
            PdfPCell offenceHeader = new PdfPCell(new Phrase("OFFENCE DETAILS", labelFont));
            offenceHeader.setColspan(2);
            offenceHeader.setBackgroundColor(new BaseColor(220, 235, 255));
            offenceHeader.setPadding(8);
            offenceHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            offenceHeader.setBorder(Rectangle.BOX);
            table.addCell(offenceHeader);
            
            addStyledTableRow(table, "Offence Date:", challan.getOffenceDate().toString(), labelFont, valueFont);
            addStyledTableRow(table, "Offence Time:", challan.getOffenceTime().toString(), labelFont, valueFont);
            addStyledTableRow(table, "Offence Place:", challan.getOffencePlace(), labelFont, valueFont);
            addStyledTableRow(table, "Offence Type:", challan.getOffenceType(), labelFont, valueFont);
            
            
            
         // ======= CHALLAN STATUS =======
            PdfPCell statusHeader = new PdfPCell(
                    new Phrase("CHALLAN STATUS", labelFont)
            );

            statusHeader.setColspan(2);
            statusHeader.setBackgroundColor(new BaseColor(220, 235, 255));
            statusHeader.setPadding(8);
            statusHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            statusHeader.setBorder(Rectangle.BOX);
            table.addCell(statusHeader);

            String challanStatus = challan.getChallanStatus() != null
                    ? challan.getChallanStatus().name()
                    : "N/A";

            addStyledTableRow(
                    table,
                    "Status:",
                    challanStatus,
                    labelFont,
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            12,
                            BaseColor.BLACK
                    )
            );
            
       
            // ======= FINE SECTION =======
            PdfPCell fineHeader = new PdfPCell(new Phrase("FINE AMOUNT", labelFont));
            fineHeader.setColspan(2);
            fineHeader.setBackgroundColor(new BaseColor(255, 220, 220));
            fineHeader.setPadding(8);
            fineHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            fineHeader.setBorder(Rectangle.BOX);
            table.addCell(fineHeader);
            
            String fineAmount = challan.getFineAmount() != null ? 
                    "₹ " + String.format("%.2f", challan.getFineAmount()) : "N/A";
            addStyledTableRow(table, "Fine Amount:", fineAmount, labelFont, 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, new BaseColor(200, 0, 0)));
            
            // Date & Time
            String dateTime = challan.getCreatedAt() != null ? 
                    challan.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")) : "N/A";
            addStyledTableRow(table, "Generated On:", dateTime, labelFont, valueFont);
            
            document.add(table);
            
            // ======= VEHICLE IMAGE (if available) =======
            if (challan.getVehicleImage() != null && challan.getVehicleImage().length > 0) {
                try {
                    document.add(new Paragraph("\n"));
                    Image img = Image.getInstance(challan.getVehicleImage());
                    img.scaleToFit(180, 180);
                    img.setAlignment(Element.ALIGN_CENTER);
                    img.setBorder(Rectangle.BOX);
                    img.setBorderColor(BaseColor.LIGHT_GRAY);
                    img.setBorderWidth(1);
                    document.add(img);
                } catch (Exception e) {
                    // Skip if image can't be loaded
                }
            }
            
            // ======= QR CODE PLACEHOLDER & SIGNATURE =======
            document.add(new Paragraph("\n"));
            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setWidthPercentage(100);
            
            // Left side - QR Code placeholder
            PdfPCell qrCell = new PdfPCell(new Phrase("[QR CODE]", 
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY)));
            qrCell.setFixedHeight(50);
            qrCell.setBorder(Rectangle.BOX);
            qrCell.setBorderColor(BaseColor.LIGHT_GRAY);
            qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            footerTable.addCell(qrCell);
            
            // Right side - Signature
            PdfPCell signCell = new PdfPCell(new Phrase("Authorized Signature", 
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.DARK_GRAY)));
            signCell.setFixedHeight(50);
            signCell.setBorder(Rectangle.BOX);
            signCell.setBorderColor(BaseColor.LIGHT_GRAY);
            signCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            signCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            footerTable.addCell(signCell);
            
            document.add(footerTable);
            
            document.add(new Paragraph("\n"));
        }
        
        // ======= BOTTOM FOOTER =======
        document.add(new Paragraph("\n"));
        PdfPTable bottomLine = new PdfPTable(1);
        bottomLine.setWidthPercentage(100);
        PdfPCell bottomLineCell = new PdfPCell();
        bottomLineCell.setBorder(Rectangle.BOTTOM);
        bottomLineCell.setBorderColor(new BaseColor(0, 51, 102));
        bottomLineCell.setBorderWidth(2);
        bottomLineCell.setPadding(0);
        bottomLineCell.setFixedHeight(3);
        bottomLine.addCell(bottomLineCell);
        document.add(bottomLine);
        
        document.add(new Paragraph(" "));
        
        Paragraph footer = new Paragraph(
            "This is a system-generated receipt. Valid for official purposes only.\nFor queries, contact Traffic Police Department.",
            footerFont
        );
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(5);
        document.add(footer);
        
        // ======= PAGE NUMBER =======
        document.add(new Paragraph("\n"));
        Paragraph pageNo = new Paragraph("Page 1 of 1", 
            FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.LIGHT_GRAY));
        pageNo.setAlignment(Element.ALIGN_RIGHT);
        document.add(pageNo);
        
        document.close();
        return baos.toByteArray();
    }
    
    /**
     * Helper method to add a styled row to the PDF table
     */
    private void addStyledTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        // Label cell
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setPadding(6);
        labelCell.setPaddingLeft(10);
        labelCell.setBackgroundColor(new BaseColor(248, 249, 250));
        labelCell.setBorder(Rectangle.BOX);
        labelCell.setBorderColor(BaseColor.LIGHT_GRAY);
        labelCell.setBorderWidth(0.5f);
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(labelCell);
        
        // Value cell
        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", valueFont));
        valueCell.setPadding(6);
        valueCell.setPaddingLeft(10);
        valueCell.setBorder(Rectangle.BOX);
        valueCell.setBorderColor(BaseColor.LIGHT_GRAY);
        valueCell.setBorderWidth(0.5f);
        valueCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(valueCell);
    }
    
    
    
    @Override
    public ChallanStatusResponseDTO getChallanStatus(String challanNumber) {
        // Validate input
        if (challanNumber == null || challanNumber.trim().isEmpty()) {
            ChallanStatusResponseDTO response = new ChallanStatusResponseDTO();
            response.setSuccess(false);
            response.setMessage("Challan number is required");
            return response;
        }
        
        // Find challan by challan number
        Optional<Challan> challanOptional = challanRepository.findByChallanNumber(challanNumber.trim());
        
        if (challanOptional.isPresent()) {
            Challan challan = challanOptional.get();
            
            ChallanStatusResponseDTO response = new ChallanStatusResponseDTO();
            response.setChallanNumber(challan.getChallanNumber());
            response.setVehicleNumber(challan.getVehicleNumber());
            response.setStatus(challan.getChallanStatus() != null ? 
                              challan.getChallanStatus().name() : "UNKNOWN");
            response.setFineAmount(challan.getFineAmount());
            response.setOwnerName(challan.getOwnerName());
            response.setOffenceType(challan.getOffenceType());
            response.setOffencePlace(challan.getOffencePlace());
            response.setSuccess(true);
            response.setMessage("Challan status retrieved successfully");
            
            return response;
        } else {
            ChallanStatusResponseDTO response = new ChallanStatusResponseDTO();
            response.setSuccess(false);
            response.setMessage("Challan not found with challan number: " + challanNumber);
            return response;
        }
    }
    
    @Override
    public List<ChallanStatusResponseDTO> getChallanStatusByVehicleNumber(String vehicleNumber) {
        List<ChallanStatusResponseDTO> responses = new ArrayList<>();
        
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            return responses;
        }
        
        List<Challan> challans = challanRepository.findByVehicleNumber(vehicleNumber.trim());
        
        for (Challan challan : challans) {
            ChallanStatusResponseDTO response = new ChallanStatusResponseDTO();
            response.setChallanNumber(challan.getChallanNumber());
            response.setVehicleNumber(challan.getVehicleNumber());
            response.setStatus(challan.getChallanStatus() != null ? 
                              challan.getChallanStatus().name() : "UNKNOWN");
            response.setFineAmount(challan.getFineAmount());
            response.setOwnerName(challan.getOwnerName());
            response.setOffenceType(challan.getOffenceType());
            response.setOffencePlace(challan.getOffencePlace());
            response.setSuccess(true);
            responses.add(response);
        }
        
        return responses;
    }
    
}
