package com.coupon.service;

import com.coupon.entity.PaymentMethodEntity;
import com.coupon.model.PaymentMethodDTO;
import com.coupon.reposistory.PaymentMethodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ModelMapper mapper;

    public PaymentMethodDTO savePaymentMethod(PaymentMethodDTO dto) {
        try {
            PaymentMethodEntity paymentMethodEntity = new PaymentMethodEntity();
            paymentMethodEntity.setPaymentType(dto.getPaymentType());
            paymentMethodEntity.setImage(dto.getImage());



            PaymentMethodEntity savedEntity = paymentMethodRepository.save(paymentMethodEntity);

            PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
            paymentMethodDTO.setPaymentType(paymentMethodEntity.getPaymentType());
            paymentMethodDTO.setImage(paymentMethodEntity.getImage());
            return paymentMethodDTO;

        } catch (Exception e) {
            // Log the exception and return a meaningful message
            e.printStackTrace(); // Log the error for debugging
            throw new RuntimeException("Failed to save payment method: " + e.getMessage());
        }
    }

    public List<PaymentMethodDTO> getAllPaymentMethods() {
        List<PaymentMethodEntity> paymentMethods = paymentMethodRepository.findAll();
        return paymentMethods.stream().map(paymentMethod -> {
            PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
            paymentMethodDTO.setId(paymentMethod.getId());
            paymentMethodDTO.setPaymentType(paymentMethod.getPaymentType());
            paymentMethodDTO.setImage(paymentMethod.getImage());
            return paymentMethodDTO;
        }).collect(Collectors.toList());
    }
}