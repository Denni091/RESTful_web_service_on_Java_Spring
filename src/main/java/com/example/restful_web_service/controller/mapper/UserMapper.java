package com.example.restful_web_service.controller.mapper;

import com.example.restful_web_service.controller.dto.UserDto;
import com.example.restful_web_service.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getEmail(),
                user.getPhone());
    }

    public User toEntity(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getAge(),
                userDto.getEmail(),
                userDto.getPhone()
        );
    }
}
