package com.example.restful_web_service.service;

import com.example.restful_web_service.controller.dto.UserDto;
import com.example.restful_web_service.entity.User;
import com.example.restful_web_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersSorted() {
        Sort sort = Sort.by("name").ascending();
        return userRepository.findAll(sort);
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public List<User> getUsersByFilter(String name, Integer age, String email, String phone) {
        return userRepository.getUserByNameOrAgeOrEmailOrPhone(name, age, email, phone);
    }

    public Long getUserCount() {
        return userRepository.count();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user, UserDto userDto) {
        user.setName(userDto.getName());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        return userRepository.save(user);
    }


    public void delete(User user) {
        userRepository.delete(user);
    }
}
