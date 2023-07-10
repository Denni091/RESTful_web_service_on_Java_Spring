package com.example.restful_web_service.repository;

import com.example.restful_web_service.entity.UserCar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCarRepository extends JpaRepository<UserCar, Integer> {

    UserCar getUserCarByCarVinCode(String vinCode);
    List<UserCar> getUserCarByUserNameAndUserEmail(String userName, String email);
    List<UserCar> getUserCarByBrandCarAndModel(String brandCar, String model);
    Optional<UserCar> getUserCarByGraduationYear(Integer graduationYear);
}
