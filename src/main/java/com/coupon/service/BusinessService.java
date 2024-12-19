package com.coupon.service;


import com.coupon.entity.*;
import com.coupon.model.BusinessDTO;
import com.coupon.reposistory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusinessService {

    @Autowired
    private UserRepository Urepo;

    @Autowired
    private BusinessRepository Brepo;

    @Autowired
    private CategoryRepository Crepo;

    @Autowired
    private ServiceRepository Srepo;

    @Autowired
    private BusinessCategoryRepository BCrepo;

    @Autowired
    private BusinessServiceRepository BSrepo;

    public BusinessDTO saveBusiness(BusinessDTO dto) {

        BusinessEntity business = new BusinessEntity();
        business.setName(dto.getName());
        business.setCountry(dto.getCountry());
        business.setCity(dto.getCity());
        business.setStreet(dto.getStreet());
        business.setAddress(dto.getAddress());
        business.setCreated_date(new Date());
        business.setImage(dto.getImage());
        business.setUser(Urepo.findById(dto.getUser_id()).orElseThrow());

        business = Brepo.save(business);

        System.out.println(dto.getCategoryId());
        if (dto.getCategoryId() != null && !dto.getCategoryId().isEmpty()) {
            for (Integer categoryId : dto.getCategoryId()) {
                CategoryEntity category = Crepo.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

                BusinessCategoryEntity businessCategory = new BusinessCategoryEntity();
                businessCategory.setBusiness(business);
                businessCategory.setCategory(category);

                 businessCategory = BCrepo.save(businessCategory);
                System.out.println("Csave: "+businessCategory.getCategory());

            }
        }

        System.out.println(dto.getServiceId());
        if (dto.getServiceId() != null && !dto.getServiceId().isEmpty()) {
            for (Integer serviceId : dto.getServiceId()) {
                ServiceEntity service = Srepo.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + serviceId));

                BusinessServiceEntity businessService = new BusinessServiceEntity();
                businessService.setBusiness(business);
                businessService.setService(service);

                businessService = BSrepo.save(businessService);
                System.out.println("Ssave: "+businessService.getService());

            }
        }

        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setCountry(business.getCountry());
        dto.setCity(business.getCity());
        dto.setStreet(business.getStreet());
        dto.setAddress(business.getAddress());
        dto.setCreated_date(business.getCreated_date());
        dto.setImage(business.getImage());//***//

        return dto;
    }

    public List<BusinessDTO> showAllBusiness() {
        List<BusinessEntity> business = Brepo.findAll();

        List<BusinessDTO> dtoList = new ArrayList<>();
        for(BusinessEntity entity: business) {
            BusinessDTO dto = new BusinessDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setCountry(entity.getCountry());
            dto.setCity(entity.getCity());
            dto.setStreet(entity.getStreet());
            dto.setAddress(entity.getAddress());
            dto.setCreated_date(entity.getCreated_date());
            dto.setImage(entity.getImage());

            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                // If the image path doesn't already start with '/business_images/', add it
                if (!dto.getImage().startsWith("/business_images/")) {
                    dto.setImage("/business_images/" + dto.getImage());
                }
            }

            List<Integer> categoryIds = BCrepo.findByBusiness(entity).stream()
                    .map(businessCategory -> businessCategory.getCategory().getId())
                    .collect(Collectors.toList());

            List<String> categoryName = categoryIds.stream()
                    .map(categoryId ->{
                        CategoryEntity category = Crepo.findById(categoryId).orElse(null);
                        return category != null ? category.getName(): null;
                            })
                    .collect(Collectors.toList());
            dto.setCategoryName(categoryName);


            List<Integer> serviceIds = BSrepo.findByBusiness(entity).stream()
                    .map(businessService -> businessService.getService().getId())
                    .collect(Collectors.toList());

            List<String> serviceName = serviceIds.stream()
                    .map(serviceId ->{
                        ServiceEntity service = Srepo.findById(serviceId).orElse(null);
                        return service != null ? service.getName(): null;
                    })
                    .collect(Collectors.toList());
            dto.setServiceName(serviceName);


            dtoList.add(dto);
        }
        return dtoList;
    }


    public BusinessDTO updateBusinessById(Integer id, BusinessDTO dto) {

        // Fetch the existing BusinessEntity by ID
        BusinessDTO updatedto = findById(id);

        // Update business details
        updatedto.setName(dto.getName());
        updatedto.setCountry(dto.getCountry());
        updatedto.setCity(dto.getCity());
        updatedto.setStreet(dto.getStreet());
        updatedto.setAddress(dto.getAddress());

        // Optionally update the created_date if needed (if it's not meant to be static)
        // business.setCreated_date(new Date()); // Uncomment this if you want to update it on each edit

        BusinessEntity business = new BusinessEntity();
        business.setName(updatedto.getName());
        business.setCountry(updatedto.getCountry());
        business.setCity(updatedto.getCity());
        business.setStreet(updatedto.getStreet());
        business.setAddress(updatedto.getAddress());

        business = Brepo.save(business); // Save the updated business entity

        // Update Business_CategoryEntity relations if category list exists
        if (dto.getCategoryId() != null && !dto.getCategoryId().isEmpty()) {
            // Remove existing categories to ensure only updated associations exist
            BCrepo.deleteByBusinessId(id); // Delete previous associations

            for (Integer categoryId : dto.getCategoryId()) {
                CategoryEntity category = Crepo.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

                BusinessCategoryEntity businessCategory = new BusinessCategoryEntity();
                businessCategory.setBusiness(business);
                businessCategory.setCategory(category);

                BCrepo.save(businessCategory); // Save the new associations
            }
        }

        // Update Business_ServiceEntity relations if service list exists
        if (dto.getServiceId() != null && !dto.getServiceId().isEmpty()) {
            // Remove existing services to ensure only updated associations exist
            BSrepo.deleteByBusinessId(id); // Delete previous associations

            for (Integer serviceId : dto.getServiceId()) {
                ServiceEntity service = Srepo.findById(serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + serviceId));

                BusinessServiceEntity businessService = new BusinessServiceEntity();
                businessService.setBusiness(business);
                businessService.setService(service);

                BSrepo.save(businessService); // Save the new associations
            }
        }

        // Return the updated BusinessDTO
        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setCountry(business.getCountry());
        dto.setCity(business.getCity());
        dto.setStreet(business.getStreet());
        dto.setAddress(business.getAddress());
        dto.setCreated_date(business.getCreated_date());

        return dto;
    }

    public BusinessDTO findById(Integer id) {
        // Fetch the BusinessEntity by ID from the repository
        BusinessEntity business = Brepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Business not found with ID: " + id));

        // Map the BusinessEntity to BusinessDTO
        BusinessDTO businessDTO = new BusinessDTO();
        businessDTO.setId(business.getId());
        businessDTO.setName(business.getName());
        businessDTO.setCountry(business.getCountry());
        businessDTO.setCity(business.getCity());
        businessDTO.setStreet(business.getStreet());
        businessDTO.setAddress(business.getAddress());
        businessDTO.setCreated_date(business.getCreated_date());
        businessDTO.setImage(business.getImage());

        if (businessDTO.getImage() != null && !businessDTO.getImage().isEmpty()) {
            // If the image path doesn't already start with '/business_images/', add it
            if (!businessDTO.getImage().startsWith("/business_images/")) {
                businessDTO.setImage("/business_images/" + businessDTO.getImage());
            }
        }

        // Set associated categories and services
        List<Integer> categoryIds = BCrepo.findCategoryIdsByBusinessId(id);
        businessDTO.setCategoryId(categoryIds);

        List<String> categoryName = categoryIds.stream()
                .map(categoryId ->{
                    CategoryEntity category = Crepo.findById(categoryId).orElse(null);
                    return category != null ? category.getName(): null;
                })
                .collect(Collectors.toList());
        businessDTO.setCategoryName(categoryName);

        List<Integer> serviceIds = BSrepo.findServiceIdsByBusinessId(id);
        businessDTO.setServiceId(serviceIds);

        List<String> serviceName = serviceIds.stream()
                .map(serviceId ->{
                    ServiceEntity service = Srepo.findById(serviceId).orElse(null);
                    return service != null ? service.getName(): null;
                })
                .collect(Collectors.toList());
        businessDTO.setServiceName(serviceName);

        return businessDTO;
    }




}





