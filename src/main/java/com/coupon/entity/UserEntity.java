package com.coupon.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true,nullable = false)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(length = 20)
    private String role;

    @Column(name = "register_date")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate registerDate;

    @PrePersist
    protected void onCreate() {
        this.registerDate = LocalDate.now(); // Automatically set the current date
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserPhotoEntity> userPhotos;  // Assuming the user can have multiple photos

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public List<UserPhotoEntity> getUserPhotos() {
        return userPhotos;
    }

    public void setUserPhotos(List<UserPhotoEntity> userPhotos) {
        this.userPhotos = userPhotos;
    }
}
