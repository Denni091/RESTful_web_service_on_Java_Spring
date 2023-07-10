package com.example.restful_web_service.service;

import com.example.restful_web_service.controller.dto.UserPassportDto;
import com.example.restful_web_service.entity.UserPassport;
import com.example.restful_web_service.repository.UserPassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPassportService {

    private final UserPassportRepository userPassportRepository;

    @Autowired
    public UserPassportService(UserPassportRepository userPassportRepository) {
        this.userPassportRepository = userPassportRepository;
    }

    public List<UserPassport> getAllInformation() {
        return userPassportRepository.findAll();
    }

    public Optional<UserPassport> getPassportInformationByNumber(Integer passportNumber) {
        return userPassportRepository.getUserPassportByPassportNumber(passportNumber);
    }

    public List<UserPassport> getPassportInformationByNationality(String nationality) {
        return userPassportRepository.getUserPassportByNationality(nationality);
    }


    public UserPassport save(UserPassport userPassport) {
        return userPassportRepository.save(userPassport);
    }

    public UserPassport updateUserPassport (UserPassport userPassport, UserPassportDto userPassportDto) {
        userPassport.setName(userPassportDto.getName());
        userPassport.setSurname(userPassportDto.getSurname());
        userPassport.setSex(userPassportDto.getSex());
        userPassport.setDateOfBirth(userPassportDto.getDateOfBirth());
        userPassport.setNationality(userPassportDto.getNationality());
        userPassport.setDateOfIssue(userPassportDto.getDateOfIssue());
        userPassport.setDateOfExpire(userPassportDto.getDateOfExpire());
        userPassport.setPassportNumber(userPassportDto.getPassportNumber());
        return userPassportRepository.save(userPassport);
    }

    public void delete(UserPassport userPassport) {
        userPassportRepository.delete(userPassport);
    }
}
