package com.coupon.service;

import com.coupon.entity.CategoryEntity;
import com.coupon.entity.CouponEntity;
import com.coupon.model.CategoryDTO;
import com.coupon.model.CouponDTO;
import com.coupon.reposistory.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepo;

//    public CouponDTO saveCoupon(CouponDTO dto){
//        CouponEntity entity = new CouponEntity();
//        entity.setCode(dto.getCode());
//        entity.setExpired_date(dto.getExpired_date());
//
//        entity = couponRepo.save(entity);
//
//
//        System.out.println("Coupon Code :  "+entity.getCode());
//        dto.setId(entity.getId());
//        dto.setCode(entity.getCode());
//        dto.setExpired_date(entity.getExpired_date());
//
//        return dto;
//    }

    public List<CouponDTO> showCouponbyUserId(Integer userId) {

        List<CouponEntity> coupon = couponRepo.findCouponsByUserId(userId);

        List<CouponDTO> dtoList = new ArrayList<>();
        for(CouponEntity entity: coupon) {

            if (entity.getTransfer_status() == true && entity.getConfirm() == false) {                CouponDTO dto = new CouponDTO();
                dto.setId(entity.getId());
                dto.setExpired_date(entity.getExpired_date());
                if (entity.getPackageEntity() != null) {
                    dto.setPackageName(entity.getPackageEntity().getName());
                    dto.setImage(entity.getPackageEntity().getImage());
                }


                System.out.println("packageName: " + dto.getPackageName());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }


}
