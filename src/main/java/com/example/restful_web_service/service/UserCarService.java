package com.example.restful_web_service.service;

import com.example.restful_web_service.controller.dto.UserCarDto;
import com.example.restful_web_service.entity.UserCar;
import com.example.restful_web_service.repository.UserCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCarService {

    private final UserCarRepository userCarRepository;

    @Autowired
    public UserCarService(UserCarRepository userCarRepository) {
        this.userCarRepository = userCarRepository;
    }

    public List<UserCar> getAllInformationUserCar() {
        return userCarRepository.findAll();
    }

    public UserCar getUserCarByVinCode(String vinCode) {
        return userCarRepository.getUserCarByCarVinCode(vinCode);
    }

    public List<UserCar> getUserByUserNameAndEmail(String userName, String email) {
        return userCarRepository.getUserCarByUserNameAndUserEmail(userName, email);
    }

    public List<UserCar> getUserCarByBrandCarAndModel(String brandCar, String model) {
        return userCarRepository.getUserCarByBrandCarAndModel(brandCar, model);
    }

    public Optional<UserCar> getUserCarByGraduationYear(Integer graduationYear) {
        return userCarRepository.getUserCarByGraduationYear(graduationYear);
    }

    public UserCar createUserCar(UserCar userCar) {
        return userCarRepository.save(userCar);
    }

    public UserCar updateUserCarInformation(String vinCode, UserCarDto userCarDto) {
        UserCar userCar = userCarRepository.getUserCarByCarVinCode(vinCode);
        userCar.setUserName(userCarDto.getUserName());
        userCar.setUserEmail(userCarDto.getUserEmail());
        userCar.setBrandCar(userCarDto.getBrandCar());
        userCar.setModel(userCarDto.getModel());
        userCar.setGraduationYear(userCarDto.getGraduationYear());
        return userCarRepository.save(userCar);
    }

    public void deleteUserCarInformation(UserCar userCar) {
        userCarRepository.delete(userCar);
    }
}


