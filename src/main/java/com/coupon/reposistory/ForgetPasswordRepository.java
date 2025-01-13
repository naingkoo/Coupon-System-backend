package com.coupon.reposistory;

import com.coupon.entity.ForgetPassword;
import com.coupon.entity.UserEntity;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Long> {
    @Query("Select fp From ForgetPassword fp where fp.user.email=:user_email")
    Optional<ForgetPassword> findOtpByEmail(@Param("user_email") String email);

}
