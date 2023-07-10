package com.example.restful_web_service.controller.mapper;

import com.example.restful_web_service.controller.dto.UserPassportDto;
import com.example.restful_web_service.entity.UserPassport;
import org.springframework.stereotype.Component;

@Component
public class UserPassportMapper {

    public UserPassportDto toDto(UserPassport userPassport) {
        return new UserPassportDto(
                userPassport.getId(),
                userPassport.getName(),
                userPassport.getSurname(),
                userPassport.getSex(),
                userPassport.getDateOfBirth(),
                userPassport.getNationality(),
                userPassport.getDateOfIssue(),
                userPassport.getDateOfExpire(),
                userPassport.getPassportNumber()
        );
    }

    public UserPassport toEntity(UserPassportDto userPassportDto) {
        return new UserPassport(
                userPassportDto.getId(),
                userPassportDto.getName(),
                userPassportDto.getSurname(),
                userPassportDto.getSex(),
                userPassportDto.getDateOfBirth(),
                userPassportDto.getNationality(),
                userPassportDto.getDateOfIssue(),
                userPassportDto.getDateOfExpire(),
                userPassportDto.getPassportNumber()
        );
    }
}
