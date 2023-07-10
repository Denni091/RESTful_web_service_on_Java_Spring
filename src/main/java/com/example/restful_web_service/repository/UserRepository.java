package com.example.restful_web_service.repository;

import com.example.restful_web_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> getUserByNameOrAgeOrEmailOrPhone(String name, Integer age, String email, String phone);
    long count();
}
