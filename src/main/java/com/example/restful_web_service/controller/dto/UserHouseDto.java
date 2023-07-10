package com.example.restful_web_service.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserHouseDto {
    private Integer id;
    private String userName;
    private String userPhone;
    private String country;
    private String town;
    private String address;
    private Integer houseNumber;
    private Integer flatNumber;
}
