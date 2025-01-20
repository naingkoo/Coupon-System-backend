package com.coupon.service;

import com.coupon.entity.BusinessEntity;
import com.coupon.entity.PackageEntity;
import com.coupon.model.PackageDTO;
import com.coupon.reposistory.BusinessRepository;
import com.coupon.reposistory.PackageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private BusinessRepository Brepo;

    public PackageDTO savePackage(PackageDTO dto) {
        try {
            PackageEntity packageEntity = new PackageEntity();
            packageEntity.setName(dto.getName());
            packageEntity.setUnit_price(dto.getUnit_price());
            packageEntity.setQuantity(dto.getQuantity());
            packageEntity.setCreate_date(dto.getCreate_date());
            packageEntity.setExpired_date(dto.getExpired_date());
            packageEntity.setImage(dto.getImage());
            packageEntity.setDescription(dto.getDescription());
            packageEntity.setBusiness(Brepo.findById(dto.getBusiness_id()).orElseThrow());

            PackageEntity savedEntity = packageRepo.save(packageEntity);

            PackageDTO updatedDto;
            updatedDto = mapper.map(savedEntity, PackageDTO.class);
            return updatedDto;

        } catch (Exception e) {
            // Log the exception and return a meaningful message
            e.printStackTrace(); // Log the error for debugging
            throw new RuntimeException("Failed to save package: " + e.getMessage());
        }
    }


    public  PackageDTO findById(Integer id){
        PackageEntity packageEntity=packageRepo.findById(id).orElseThrow();

        PackageDTO dto=mapper.map(packageEntity,PackageDTO.class);
        return dto;


    }

    public List<PackageDTO> getAllpackage() {
        // Fetch all package entities from the repository
        List<PackageEntity> packageEntity = packageRepo.findAllActive();
        // Convert the list of PackageEntity to a list of PackageDTO
        List<PackageDTO> dtoList = packageEntity.stream().map(pkg -> {
            // Map the entity to DTO
            PackageDTO dto = mapper.map(pkg, PackageDTO.class);

            // Ensure the image path is valid and starts with '/images/'
            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                // If the image path doesn't already start with '/images/', add it
                if (!dto.getImage().startsWith("/images/")) {
                    dto.setImage(dto.getImage());
                }
            }

            return dto;
        }).toList();

        // Return the transformed list of DTOs
        return dtoList;
    }

    public long countAll(){
        return packageRepo.countByIsDeleteFalse();
    }

    public List<PackageDTO> getByBusinessId(Integer id) {
        // Check if the id is null and handle appropriately
        if (id == null) {
            throw new IllegalArgumentException("Business ID cannot be null.");
        }

        // Log the business ID being processed
        System.out.println("Fetching packages for Business ID: " + id);

        // Fetch all package entities from the repository
        List<PackageEntity> packageEntities = packageRepo.findByBusinessId(id);

        // Handle case where no packages are found for the given business ID
        if (packageEntities == null || packageEntities.isEmpty()) {
            System.out.println("No packages found for Business ID: " + id);
            return Collections.emptyList();
        }

        // Convert the list of PackageEntity to a list of PackageDTO
        List<PackageDTO> dtoList = packageEntities.stream()
                .map(pkg -> {
                    // Map the entity to DTO
                    PackageDTO dto = mapper.map(pkg, PackageDTO.class);

                    // Ensure the image path is valid and starts with '/images/'
                    if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                        if (!dto.getImage().startsWith("/images/")) {
                            dto.setImage(dto.getImage());
                        }
                    }

                    return dto;
                })
                .toList();

        // Log the number of packages found
        System.out.println("Number of packages found: " + dtoList.size());

        // Return the transformed list of DTOs
        return dtoList;
    }

    public List<PackageDTO> getPackagesByBusinessName(String businessName) {
        // Fetch packages by business name
        List<PackageEntity> packageEntities = packageRepo.findByBusinessName(businessName);

        // Convert package entities to DTOs
        List<PackageDTO> packageDTOs = new ArrayList<>();
        for (PackageEntity packageEntity : packageEntities) {
            PackageDTO dto = new PackageDTO();
            dto.setId(packageEntity.getId());
            dto.setName(packageEntity.getName());
            dto.setUnit_price(packageEntity.getUnit_price());
            dto.setQuantity(packageEntity.getQuantity());
            dto.setImage(packageEntity.getImage());
            dto.setDescription(packageEntity.getDescription());
            dto.setDeleted(packageEntity.isDelete());
            dto.setBusiness_id(packageEntity.getBusiness() != null ? packageEntity.getBusiness().getId() : null);

            packageDTOs.add(dto);
        }

        return packageDTOs;
    }

    public PackageDTO updateById(Integer id, PackageDTO dto) {
        PackageDTO resDTO = findById(id);

        BusinessEntity currentBusiness = Brepo.findById(dto.getBusiness_id())
                .orElseThrow(() -> new RuntimeException("Business with ID " + dto.getBusiness_id() + " not found"));

        resDTO.setName(dto.getName());
        resDTO.setUnit_price(dto.getUnit_price());
        resDTO.setQuantity(dto.getQuantity());
        resDTO.setDescription(dto.getDescription());
        resDTO.setCreate_date(dto.getCreate_date());

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            resDTO.setImage(dto.getImage());
        }

        PackageEntity reqDTO = mapper.map(resDTO, PackageEntity.class);
        reqDTO.setBusiness(currentBusiness);

        packageRepo.save(reqDTO);

        resDTO = mapper.map(reqDTO, PackageDTO.class);

        return resDTO;
    }

    public void softDeletePackage(Integer id) {
        PackageEntity packageEntity = packageRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        packageEntity.setDelete(true);
        packageRepo.save(packageEntity);
    }

    public long countByBusinessId(Long businessId) {
        return packageRepo.countPackageByBusinessId(businessId);
    }
}

