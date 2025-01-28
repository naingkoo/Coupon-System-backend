package com.coupon.reposistory;
import com.coupon.entity.BusinessEntity;
import com.coupon.entity.UserEntity;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Integer id);
    long countByIsdeleteFalse();

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.password = :password WHERE u.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    @Query("SELECT u.email FROM UserEntity u WHERE LOWER(u.email) LIKE :query")
    List<String> findEmailsByQuery(String query);

    @Modifying
    @Query("UPDATE UserEntity u SET u.isdelete = true WHERE u.id = :id")
    void deleteUserById(@Param("id") Integer id);


    @Query("SELECT u FROM UserEntity u WHERE u.isdelete = false")
    List<UserEntity> findAllActiveUser();

}