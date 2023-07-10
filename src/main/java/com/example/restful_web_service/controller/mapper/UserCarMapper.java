package com.example.restful_web_service.controller.mapper;

import com.example.restful_web_service.controller.dto.UserCarDto;
import com.example.restful_web_service.entity.UserCar;
import org.springframework.stereotype.Component;

@Component
public class UserCarMapper {

    public UserCarDto toDto(UserCar userCar) {
        return new UserCarDto(
                userCar.getId(),
                userCar.getUserName(),
                userCar.getUserEmail(),
                userCar.getBrandCar(),
                userCar.getGraduationYear(),
                userCar.getModel(),
                userCar.getCarVinCode()
        );
    }

    public UserCar toEntity(UserCarDto userCarDto) {
        return new UserCar(
                userCarDto.getId(),
                userCarDto.getUserName(),
                userCarDto.getUserEmail(),
                userCarDto.getBrandCar(),
                userCarDto.getGraduationYear(),
                userCarDto.getModel(),
                userCarDto.getCarVinCode()
        );
    }
}
