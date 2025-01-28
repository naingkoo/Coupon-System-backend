package com.coupon.controller;

import com.coupon.entity.BusinessEntity;
import com.coupon.model.BusinessDTO;
import com.coupon.model.PackageDTO;
import com.coupon.model.ReviewDTO;
import com.coupon.service.BusinessService;
import com.coupon.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/Business")
public class BusinessController {

    @Autowired
    private BusinessService Bservice;

    @PostMapping("/create")
    public ResponseEntity<BusinessDTO> saveBusiness(@RequestPart BusinessDTO dto,
                                                    @RequestPart MultipartFile image) {

        try {
            if (!image.isEmpty()) {
                // Define the directory to store images (stored in resources folder)
                String uploadDir = new File("src/main/resources/static/business_images").getAbsolutePath();
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                // String contentType = image.getContentType();

                String fileName = image.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;

                File file = new File(filePath);
                image.transferTo(file);

                dto.setImage("business_images/" + fileName);  // Ensure the image path is prefixed with `/images/`
            }

            BusinessDTO businessdto = Bservice.saveBusiness(dto);

            return new ResponseEntity<>(businessdto, HttpStatus.CREATED);

        } catch (IOException e) {
            // Log and handle IO exception
            System.out.println("Here is an error: " + e.getMessage()
            );
          //  e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Return a 500 Internal Server Error
        } catch (Exception ex) {
            System.out.println("Here is an error: " + ex.getMessage());
          //  ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Return a 400 Bad Request error
        }
    }

    @GetMapping("public/list")
    public List<BusinessDTO> getAllBusiness() {
        return Bservice.getActiveBusiness();
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<BusinessDTO> getBusinessByUserId(@PathVariable("userId") Integer userId) {
        try {
            // Fetch the business by ID using the service layer
            BusinessDTO businessDTO = Bservice.findByUserId(userId);

            // Return the business details if found
            return ResponseEntity.ok(businessDTO);
        } catch (IllegalArgumentException e) {
            // Handle error if business not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/countALlBusiness")
    public long countALlUser() {
        return Bservice.countAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<BusinessDTO> getBusinessById(@PathVariable("id") Integer id) {
        try {
            // Fetch the business by ID using the service layer
            BusinessDTO businessDTO = Bservice.findById(id);

            // Return the business details if found
            return ResponseEntity.ok(businessDTO);
        } catch (IllegalArgumentException e) {
            // Handle error if business not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "hey this is test";
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<BusinessDTO> updateBusiness(
            @PathVariable Integer id,
            @RequestPart("dto") String businessJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Convert JSON string to BusinessDTO
            ObjectMapper objectMapper = new ObjectMapper();
            BusinessDTO businessDTO = objectMapper.readValue(businessJson, BusinessDTO.class);

            if (image != null && !image.isEmpty()) {
                String uploadDir = new File("src/main/resources/static/business_images").getAbsolutePath();
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String fileName = image.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                image.transferTo(new File(filePath));

                businessDTO.setImage("business_images/" + fileName);
            }

            // Update business
            BusinessDTO updatedBusiness = Bservice.updateBusinessById(id, businessDTO);

            return new ResponseEntity<>(updatedBusiness, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/delete/{id}")
    public void deleteBusiness(@PathVariable Integer id) {
        Bservice.deleteBusinessById(id);
    }


    @GetMapping("/{categoryName}")
    public ResponseEntity<List<BusinessEntity>> getBusinessByCatergoryName(@PathVariable("categoryName") String catergoryName) {
        List<BusinessEntity> businesses = Bservice.getBusinessByCategoryName(catergoryName);
        if(businesses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(businesses, HttpStatus.OK);
    }

    @GetMapping("/deleted")
    public List<BusinessDTO> getDeletedBusinesses() {
        // Call the service method and return the list of deleted businesses
        return Bservice.getDeletedBusiness();
    }

    @PutMapping("/restore/{id}")
    public void restoreBusiness(@PathVariable Integer id) {
        Bservice.restoreBusiness(id);
    }

    @GetMapping("/names")
    public List<BusinessDTO> getAllBusinessNamesAndIds() {
        return Bservice.getAllBusinessNamesAndIds();
    }

//    @PutMapping("closeBusiness/{id}")
//    public ResponseEntity<String> closeBusiness(@PathVariable("id") Integer id) {
//        boolean isClosed = Bservice.closeBusiness(id);
//        if (isClosed) {
//            return ResponseEntity.ok("Business with ID " + id + " is now closed.");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Failed to close the business with ID " + id + ".");
//        }
//    }
//
//
//    @PutMapping("openBusiness/{id}")
//    public ResponseEntity<String> openBusiness(@PathVariable("id") Integer id) {
//        boolean isOpened = Bservice.openBusiness(id);
//        if (isOpened) {
//            return ResponseEntity.ok("Business with ID " + id + " is now open.");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Failed to open the business with ID " + id + ".");
//        }
//    }

    @PutMapping("toggle/{id}")
    public ResponseEntity<Map<String, String>> toggleBusinessStatus(@PathVariable("id") Integer id) {
        boolean isToggled = Bservice.toggleBusinessStatus(id);
        Map<String, String> response = new HashMap<>();
        if (isToggled) {
            response.put("message", "Business with ID " + id + " status has been toggled.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to toggle the business with ID " + id + ".");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


}

