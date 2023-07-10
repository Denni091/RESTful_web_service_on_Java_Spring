package com.example.restful_web_service.repository;

import com.example.restful_web_service.entity.UserHouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserHouseRepository extends JpaRepository<UserHouse, Integer> {

    Optional<UserHouse> getUserHouseByHouseNumberAndFlatNumber(Integer houseNumber, Integer flatNumber);

    List<UserHouse> getUserHouseByTown(String town);
    List<UserHouse> getUserHouseByCountry(String country);
}
