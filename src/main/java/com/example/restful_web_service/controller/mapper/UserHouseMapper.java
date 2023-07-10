package com.example.restful_web_service.controller.mapper;

import com.example.restful_web_service.controller.dto.UserHouseDto;
import com.example.restful_web_service.entity.UserHouse;
import org.springframework.stereotype.Component;

@Component
public class UserHouseMapper {

    public UserHouseDto toDto(UserHouse userHouse) {
        return new UserHouseDto(
                userHouse.getId(),
                userHouse.getUserName(),
                userHouse.getUserPhone(),
                userHouse.getCountry(),
                userHouse.getTown(),
                userHouse.getAddress(),
                userHouse.getHouseNumber(),
                userHouse.getFlatNumber()
        );
    }

    public UserHouse toEntity(UserHouseDto userHouseDto) {
        return new UserHouse(
                userHouseDto.getId(),
                userHouseDto.getUserName(),
                userHouseDto.getUserPhone(),
                userHouseDto.getCountry(),
                userHouseDto.getTown(),
                userHouseDto.getAddress(),
                userHouseDto.getHouseNumber(),
                userHouseDto.getFlatNumber()
        );
    }
}
