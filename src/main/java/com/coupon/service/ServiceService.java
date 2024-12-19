package com.coupon.service;

import com.coupon.entity.ServiceEntity;
import com.coupon.model.ServiceDTO;
import com.coupon.reposistory.ServiceRepository;
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

        List<ServiceEntity> service = Srepo.findAll();

        List<ServiceDTO> dtoList = new ArrayList<>();
        for(ServiceEntity entity: service) {
            ServiceDTO dto = new ServiceDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());

            dtoList.add(dto);
        }
        return dtoList;  
    }

}
