package com.coupon.controller;

import com.coupon.model.ServiceDTO;
import com.coupon.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Service")
public class ServiceController {

    @Autowired
    private ServiceService Sservice;

    @PostMapping("/create")
    public ServiceDTO saveService(@RequestBody ServiceDTO dto){


        return  Sservice.saveService(dto);
    }

    @GetMapping("/public/list")
    public List<ServiceDTO> shwoAllService() {
        return Sservice.showAllService();
    }

}
