package com.coupon.controller;

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
@CrossOrigin
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

                packageDTO.setImage("/images/" + fileName);  // Ensure the image path is prefixed with `/images/`
            }

            PackageDTO savedPackage = packageService.savePackage(packageDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPackage);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/find/{id}")
    public PackageDTO findById(@PathVariable("id") Integer id){
        return packageService.findById(id);
    }

    @GetMapping("/findByBusinessId/{id}")
    public List<PackageDTO> findByBusinessId(@PathVariable("id") Integer id) {

        System.out.println(id);
        // Fetch the list of packages based on the business ID from the service
        return packageService.getByBusinessId(id);
    }


    @GetMapping("/public/list")
    public List<PackageDTO> getAll(){
        return packageService.getAllpackage();
    }

}

