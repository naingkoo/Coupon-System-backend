package com.coupon.service;

import com.coupon.entity.ServiceEntity;
import com.coupon.model.ServiceDTO;
import com.coupon.reposistory.ServiceRepository;
import com.coupon.responObject.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository Srepo;

    @Autowired
    private ModelMapper mapper;

    public ServiceDTO saveService(ServiceDTO dto){
        ServiceEntity service = new ServiceEntity();
        service.setName(dto.getName());

        service = Srepo.save(service);

        dto.setId(service.getId());
        dto.setName(service.getName());
        return dto;

    }

    public List<ServiceDTO> showAllService() {

        List<ServiceEntity> service = Srepo.findAllActiveServices();

        List<ServiceDTO> dtoList = new ArrayList<>();
        for(ServiceEntity entity: service) {
            ServiceDTO dto = new ServiceDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());

            dtoList.add(dto);
        }
        return dtoList;  
    }

    public ServiceDTO updateServiceById(Integer id, ServiceDTO dto) {


        ServiceEntity existingService =Srepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        // Update the existing service with data from the DTO
        existingService.setName(dto.getName());

        // Add more fields as needed

        // Save the updated entity
        ServiceEntity updatedService = Srepo.save(existingService);

        // Convert the entity back to a DTO and return
        return mapper.map(updatedService, ServiceDTO.class);
    }

    public ServiceDTO findById(Integer id) {
        // Fetch the ServiceEntity by ID from the repository
        ServiceEntity service = Srepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + id));

        // Map the ServiceEntity to ServiceDTO
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(service.getId());
        serviceDTO.setName(service.getName());

        return serviceDTO;
    }

    public void softDeleteService(Integer id) {
        ServiceEntity service = Srepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        //category.setDelete(true);  // Assuming you have a 'deleted' flag
        Srepo.softDeleted(id);
    }

}
