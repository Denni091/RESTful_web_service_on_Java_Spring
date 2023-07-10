package com.example.restful_web_service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCarDto {
    private Integer id;
    private String userName;
    private String userEmail;
    private String brandCar;
    private Integer graduationYear;
    private String model;
    private String carVinCode;
}
