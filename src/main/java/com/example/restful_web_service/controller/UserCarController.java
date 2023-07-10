package com.example.restful_web_service.controller;

import com.example.restful_web_service.controller.dto.UserCarDto;
import com.example.restful_web_service.controller.mapper.UserCarMapper;
import com.example.restful_web_service.entity.UserCar;
import com.example.restful_web_service.service.UserCarService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/car")
public class UserCarController {

    private final UserCarMapper userCarMapper;
    private final UserCarService userCarService;
    Logger logger = Logger.getLogger(UserCarController.class);

    public UserCarController(UserCarMapper userCarMapper, UserCarService userCarService) {
        this.userCarMapper = userCarMapper;
        this.userCarService = userCarService;
    }

    @GetMapping
    public List<UserCarDto> getAllInformationUserCar() {
        try {
            List<UserCar> userCars = userCarService.getAllInformationUserCar();
            if (userCars.isEmpty()) {
                logger.warn("No user car information is empty");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user car information is empty");
            }
            logger.info("All information retrieved from data base");
            return userCars.
                    stream().
                    map(userCarMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error get all information from data base");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage());
        }
    }

    @GetMapping("/{vinCode}")
    public UserCarDto getUserCarByVinCode(@PathVariable String vinCode) {
        try {
            UserCar userCars = userCarService.getUserCarByVinCode(vinCode);
            if (vinCode.isEmpty()) {
                logger.warn("No user car information is empty");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No user car information is empty");
            }
            if (!vinCode.matches("^[A-Za-z0-9]+$")) {
                logger.warn("VIN code contains invalid characters");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "VIN code contains invalid characters");
            }
            return userCarMapper.toDto(userCars);
        } catch (Exception e) {
            logger.error("Error getting information by vin code: " + vinCode);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/graduationYear/{graduationYear}")
    public UserCarDto getUserCarByGraduationYear(@PathVariable Integer graduationYear) {
        try {
            Optional<UserCar> userCarOptional = userCarService.getUserCarByGraduationYear(graduationYear);
            UserCar userCar = userCarOptional.orElseThrow(() -> {
                logger.warn("User car information with this graduation year: " + graduationYear + " not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User car information with this graduation year: " + graduationYear + " not found");
            });
            if (!graduationYear.toString().matches("^\\d{4}$")) {
                logger.warn("Graduation year has invalid format: " + graduationYear);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Graduation year has invalid format: " + graduationYear);
            }
            return userCarMapper.toDto(userCar);
        } catch (Exception e) {
            logger.error("Error getting user car information by graduation year");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/userName/{userName}/email/{email}")
    public List<UserCarDto> getUserCarByUserNameAndEmail(@PathVariable String userName, @PathVariable String email) {
        try {
            List<UserCar> userCars = userCarService.getUserByUserNameAndEmail(userName, email);
            if (userCars.isEmpty()) {
                logger.warn("User car information by name and email is empty");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "User car information by name and email is empty");
            }
            if (!userName.matches("^[A-Za-z0-9]+$")) {
                logger.warn("Invalid characters in user name: " + userName);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid characters in user name: " + userName);
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                logger.warn("Invalid email address format: " + email);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address format: " + email);
            }
            return userCars.
                    stream().
                    map(userCarMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting information by user name and email: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting information by user name and email: " + e.getMessage());
        }
    }

    @GetMapping("/brancCar/{brandCar}/model/{model}")
    public List<UserCarDto> getUserCarByBrandCarAndModel(@PathVariable String brandCar, @PathVariable String model) {
        try {
            List<UserCar> userCars = userCarService.getUserCarByBrandCarAndModel(brandCar, model);
            if (userCars.isEmpty()) {
                logger.warn("User car information by brand car and model is empty");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "User car information by brand car and model is empty");
            }
            if (!brandCar.matches("^[a-zA-Z\\s]+$")) {
                logger.warn("Invalid characters in brand car: " + brandCar);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid characters in brand car: " + brandCar);
            }
            if (!model.matches("^[a-zA-Z0-9\\s]+$")) {
                logger.warn("Invalid characters in model car: " + model);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid characters in model car: " + model);
            }
            return userCars.
                    stream().
                    map(userCarMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting user car information by brand car and model: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting user car information by brand car and model: " + e.getMessage());
        }
    }

    @PostMapping
    public UserCarDto createUserCar(@RequestBody UserCarDto userCarDto) {
        try {
            if (userCarDto.getUserName() != null && !userCarDto.getUserName().matches("^[A-Za-z0-9]+$")) {
                logger.warn("Invalid characters in user name: " + userCarDto.getUserName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid characters in user name: " + userCarDto.getUserName());
            }
            if (userCarDto.getUserEmail() != null &&
                    !userCarDto.getUserEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                logger.warn("Invalid email address format: " + userCarDto.getUserEmail());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid email address format: " + userCarDto.getUserEmail());
            }
            if (userCarDto.getGraduationYear() != null &&
                    !userCarDto.getGraduationYear().toString().matches("^\\d{4}$")) {
                logger.warn("Graduation year has invalid format: " + userCarDto.getGraduationYear());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Graduation year has invalid format: " + userCarDto.getGraduationYear());
            }
            if (userCarDto.getModel() != null && !userCarDto.getModel().matches("^[a-zA-Z0-9\\s]+$")) {
                logger.warn("Invalid model characters: " + userCarDto.getModel());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid model characters: " + userCarDto.getModel());
            }
            if (userCarDto.getCarVinCode() != null && !userCarDto.getCarVinCode().matches("^[A-Za-z0-9]+$")) {
                logger.warn("VIN code contains invalid characters");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "VIN code contains invalid characters");
            }
            UserCar userCar = userCarMapper.toEntity(userCarDto);
            UserCar createdUserCar = userCarService.createUserCar(userCar);
            logger.info("User car with VIN code: " + userCar.getCarVinCode() + " created");
            return userCarMapper.toDto(createdUserCar);
        } catch (Exception e) {
            logger.error("Error creating user car: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating user car: " + e.getMessage());
        }
    }


    @PutMapping("/update/{vinCode}")
    public UserCarDto updateUserCarInformation(@PathVariable String vinCode, @RequestBody UserCarDto userCarDto) {
        try {
            if (vinCode != null && !vinCode.matches("^[A-Za-z0-9]+$")) {
                logger.warn("VIN code contains invalid characters: " + vinCode);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "VIN code contains invalid characters: " + vinCode);
            }
            if (userCarDto.getUserName() != null &&
                    !userCarDto.getUserName().matches("^[A-Za-z0-9]+$") && userCarDto.getUserName().isEmpty()) {
                logger.warn("Invalid characters in user name: " + userCarDto.getUserName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid characters in user name: " + userCarDto.getUserName());
            }
            if (userCarDto.getUserEmail() != null &&
                    !userCarDto.getUserEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
                    && userCarDto.getUserEmail().isEmpty()) {
                logger.warn("Invalid characters in user email: " + userCarDto.getUserEmail());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid characters in user email: " + userCarDto.getUserEmail());
            }
            if (userCarDto.getBrandCar() != null && !userCarDto.getBrandCar().matches("^[a-zA-Z\\s]+$")) {
                logger.warn("Invalid characters in user email: " + userCarDto.getBrandCar());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid characters in user email: " + userCarDto.getBrandCar());
            }
            if (userCarDto.getModel() != null && !userCarDto.getModel().matches("^[a-zA-Z0-9\\s]+$")) {
                logger.warn("Invalid characters in model car: " + userCarDto.getModel());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid characters in model car: " + userCarDto.getModel());
            }
            if (userCarDto.getGraduationYear() != null &&
                    !userCarDto.getGraduationYear().toString().matches("^\\d{4}$")) {
                logger.warn("Graduation year has invalid format: " + userCarDto.getGraduationYear());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Graduation year has invalid format: " + userCarDto.getGraduationYear());
            }
            UserCar userCar = userCarService.updateUserCarInformation(vinCode, userCarDto);
            logger.info("Updating information with VIN Code: " + vinCode);
            return userCarMapper.toDto(userCar);
        } catch (Exception e) {
            logger.error("Error updating user car information: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating user car information: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{vinCode}")
    public String deleteUserCarInformation(@PathVariable String vinCode) {
        try {
            if (!vinCode.matches("^[A-Za-z0-9]+$")) {
                logger.warn("VIN code contains invalid characters: " + vinCode);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "VIN code contains invalid characters: " + vinCode);
            }
            UserCar userCar = userCarService.getUserCarByVinCode(vinCode);
            userCarService.deleteUserCarInformation(userCar);
            logger.info("User car with VIN Code: " + vinCode + " successful deleted");
            return "User car with VIN Code: " + vinCode + " successful deleted";
        } catch (Exception e) {
            logger.error("Error deleting user car information: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting user car information: " + e.getMessage());
        }
    }
}
