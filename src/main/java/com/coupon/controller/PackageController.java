package com.coupon.controller;

import com.coupon.entity.PackageEntity;
import com.coupon.model.PackageDTO;
import com.coupon.reposistory.BusinessRepository;
import com.coupon.reposistory.PackageRepository;
import com.coupon.service.PackageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/package")
@CrossOrigin(origins = "http://localhost:4200")
public class PackageController {

    @Autowired
    private PackageRepository packageRepo;
    @Autowired
    private BusinessRepository businessRepo;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ModelMapper mapper;

    @GetMapping("/index")
    public String index() {
        return "This is package";
    }

    @GetMapping("/public/list")
    public ResponseEntity<List<PackageDTO>> getAll() {
        List<PackageDTO> packageList = packageService.getAllpackage();
        if (packageList.isEmpty()) {
            throw new RuntimeException("No packages found");
        }
        return ResponseEntity.ok(packageList);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<PackageDTO> findById(@PathVariable("id") Integer id) {
        PackageEntity packageEntity = packageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Package with ID " + id + " not found"));

        // Map PackageEntity to PackageDTO
        PackageDTO dto = mapper.map(packageEntity, PackageDTO.class);

        // Manually set the businessId in the DTO (assuming business is not null)
        if (packageEntity.getBusiness() != null) {
            dto.setBusiness_id(packageEntity.getBusiness().getId());
        }

        return ResponseEntity.ok(dto);
    }


    @GetMapping("/findByBusinessId/{id}")
    public List<PackageDTO> findByBusinessId(@PathVariable("id") Integer id) {
        System.out.println(id);
        // Fetch the list of packages based on the business ID from the service
        return packageService.getByBusinessId(id);
    }

    @GetMapping("/findByBusinessName/{businessName}")
    public List<PackageDTO> findByBusinessName(@PathVariable("businessName") String businessName) {

        System.out.println(businessName);
        // Fetch the list of packages based on the business ID from the service
        return packageService.getPackagesByBusinessName(businessName);
    }

    @PostMapping("/save")
    public ResponseEntity<PackageDTO> createPackage(
            @RequestPart PackageDTO packageDTO,
            @RequestPart MultipartFile image) {
        try {
            if (!image.isEmpty()) {
                String uploadDir = new File("src/main/resources/static/images").getAbsolutePath();
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String fileName = image.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                File file = new File(filePath);
                image.transferTo(file);

                packageDTO.setImage("images/" + fileName);  // Ensure the image path is prefixed with `/images/`
            }

            PackageDTO savedPackage = packageService.savePackage(packageDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPackage);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PackageDTO> updatePackage(
            @PathVariable("id") Integer id,
            @RequestPart("packageDTO") PackageDTO packageDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                String uploadDir = new File("src/main/resources/static/images").getAbsolutePath();
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                String fileName = image.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                File file = new File(filePath);
                image.transferTo(file);

                packageDTO.setImage("images/"+fileName);
            }

            PackageDTO updatedPackage = packageService.updateById(id, packageDTO);
            return ResponseEntity.ok(updatedPackage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> softDeletePackage(@PathVariable Integer id) {
        if (!packageRepo.existsById(id)) {
            throw new RuntimeException("Package with ID " + id + " not found");
        }

        packageService.softDeletePackage(id);
        return ResponseEntity.ok("Package with ID " + id + " was successfully soft deleted.");
    }

    @GetMapping("/countALlPackages")
    public long countALlUser(){
        return packageService.countAll();
    }


    @GetMapping("/countPackagesByBusinessId/{businessId}")
    public long countPackagesByBusinessId(@PathVariable Long businessId) {
        return packageService.countByBusinessId(businessId);
    }
}