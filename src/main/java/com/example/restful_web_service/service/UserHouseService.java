package com.example.restful_web_service.service;

import com.example.restful_web_service.controller.dto.UserHouseDto;
import com.example.restful_web_service.entity.UserHouse;
import com.example.restful_web_service.repository.UserHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserHouseService {

    private final UserHouseRepository userHouseRepository;

    @Autowired
    public UserHouseService(UserHouseRepository userHouseRepository) {
        this.userHouseRepository = userHouseRepository;
    }

    public List<UserHouse> getAllInformationUserHouse() {
        return userHouseRepository.findAll();
    }

    public Optional<UserHouse> getInformationByHouseNumberAndFlatNumber(Integer houseNumber, Integer flatNumber) {
        return userHouseRepository.getUserHouseByHouseNumberAndFlatNumber(houseNumber, flatNumber);
    }

    public List<UserHouse> getHouseInTown(String town) {
        return userHouseRepository.getUserHouseByTown(town);
    }

    public List<UserHouse> getUserHousesInCountry(String country) {
        return userHouseRepository.getUserHouseByCountry(country);
    }

    public UserHouse save(UserHouse userHouse) {
        return userHouseRepository.save(userHouse);
    }

    public UserHouse updateUserHouse(UserHouse userHouse, UserHouseDto userHouseDto) {
        userHouse.setUserName(userHouseDto.getUserName());
        userHouse.setUserPhone(userHouseDto.getUserPhone());
        userHouse.setCountry(userHouseDto.getCountry());
        userHouse.setTown(userHouseDto.getAddress());
        userHouse.setAddress(userHouseDto.getAddress());
        userHouse.setHouseNumber(userHouseDto.getHouseNumber());
        userHouse.setFlatNumber(userHouseDto.getFlatNumber());
        return userHouseRepository.save(userHouse);
    }

    public void deleteUserHouse(UserHouse userHouse) {
        userHouseRepository.delete(userHouse);
    }
}
