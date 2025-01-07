package com.coupon.controller;

import com.coupon.entity.BusinessEntity;
import com.coupon.model.BusinessDTO;
import com.coupon.model.PackageDTO;
import com.coupon.model.ReviewDTO;
import com.coupon.service.BusinessService;
import com.coupon.service.ReviewService;
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
import java.util.List;

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

                dto.setImage("/business_images/" + fileName);  // Ensure the image path is prefixed with `/images/`
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

    @GetMapping("/countALlBusiness")
    public long countALlUser(){
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
    public String test(){
        return "hey this is test";
    }

    @PutMapping("/edit/{id}")
    public BusinessDTO updatebyId(@PathVariable("id")Integer id,@RequestBody BusinessDTO dto) {

        return Bservice.updateBusinessById(id,dto);
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

}

