package com.coupon.service;

import com.coupon.entity.CategoryEntity;
import com.coupon.entity.PaymentEntity;
import com.coupon.model.CategoryDTO;
import com.coupon.model.PaymentDTO;
import com.coupon.reposistory.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    public PaymentDTO savePayment(PaymentDTO dto) {

        PaymentEntity payment = new PaymentEntity();
        payment.setTotal_amount(dto.getTotal_amount());
        payment.setPaymentType(dto.getPaymentType());
        payment.setTransaction_id(dto.getTransaction_id());
        payment.setPayment_date(new Date());

        System.out.println("DTOpaymentType: " + dto.getPaymentType());

        payment = paymentRepo.save(payment);

        System.out.println("paymentType: " + payment.getPaymentType());

        dto.setId(payment.getId());
        dto.setTotal_amount(payment.getTotal_amount());
        dto.setPaymentType(payment.getPaymentType());
        dto.setTransaction_id(payment.getTransaction_id());

        return dto;
    }


    public List<PaymentDTO> showAllPayment() {

        List<PaymentEntity> payment = paymentRepo.findAll();

        List<PaymentDTO> dtoList = new ArrayList<>();
        for(PaymentEntity entity: payment) {
            PaymentDTO dto = new PaymentDTO();
            dto.setId(entity.getId());
            dto.setPaymentType(entity.getPaymentType());
            dto.setTransaction_id(entity.getTransaction_id());
            dto.setTotal_amount(entity.getTotal_amount());


            dtoList.add(dto);
        }
         return dtoList;
    }

}
