package com.example.restful_web_service.repository;

import com.example.restful_web_service.entity.UserPassport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPassportRepository extends JpaRepository<UserPassport, Integer> {

    Optional<UserPassport> getUserPassportByPassportNumber(Integer passportNumber);

    List<UserPassport> getUserPassportByNationality(String nationality);
}
