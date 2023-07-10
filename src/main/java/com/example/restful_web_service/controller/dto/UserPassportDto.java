package com.example.restful_web_service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPassportDto {
    private Integer id;
    private String name;
    private String surname;
    private String sex;
    private String dateOfBirth;
    private String nationality;
    private String dateOfIssue;
    private String dateOfExpire;
    private Integer passportNumber;
}
