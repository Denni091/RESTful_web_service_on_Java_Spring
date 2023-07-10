package com.example.restful_web_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_car")
public class UserCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;
    @Column(name = "brand_car", nullable = false)
    private String brandCar;
    @Column(name = "graduation_year", nullable = false)
    private Integer graduationYear;
    @Column(nullable = false)
    private String model;
    @Column(name = "car_vin_code", nullable = false, unique = true)
    private String carVinCode;


    @ManyToOne
    @JoinColumn(name = "user_car_id")
    private User user;

    public UserCar(Integer id, String userName, String userEmail,
                   String brandCar, Integer graduationYear, String model, String carVinCode) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.brandCar = brandCar;
        this.graduationYear = graduationYear;
        this.model = model;
        this.carVinCode = carVinCode;
    }
}
