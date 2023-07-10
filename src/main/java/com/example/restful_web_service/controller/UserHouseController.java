package com.example.restful_web_service.controller;

import com.example.restful_web_service.controller.dto.UserHouseDto;
import com.example.restful_web_service.controller.enums.SupportedCountries;
import com.example.restful_web_service.controller.mapper.UserHouseMapper;
import com.example.restful_web_service.entity.UserHouse;
import com.example.restful_web_service.service.UserHouseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/house")
public class UserHouseController {

    private final UserHouseService userHouseService;
    private final UserHouseMapper userHouseMapper;
    private final Logger logger = Logger.getLogger(UserHouseController.class);

    @Autowired
    public UserHouseController(UserHouseService userHouseService, UserHouseMapper userHouseMapper) {
        this.userHouseService = userHouseService;
        this.userHouseMapper = userHouseMapper;
    }

    @GetMapping
    public List<UserHouseDto> getAllInformationUserHouse() {
        try {
            List<UserHouse> userHouses = userHouseService.getAllInformationUserHouse();
            if (userHouses.isEmpty()) {
                logger.warn("User house details not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User house details not found");
            }
            logger.info("User house information retrieved from data base");
            return userHouses.
                    stream().
                    map(userHouseMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving information from data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving information from data base: " + e.getMessage());
        }
    }

    @GetMapping("/{houseNumber}/{flatNumber}")
    public UserHouseDto getInformationByHouseNumberAndFlatNumber(@PathVariable Integer houseNumber,
                                                                 @PathVariable Integer flatNumber) {
        try {
            Optional<UserHouse> userHouseOptional = userHouseService.
                    getInformationByHouseNumberAndFlatNumber(houseNumber, flatNumber);
            UserHouse userHouse = userHouseOptional.orElseThrow(() -> {
                logger.warn("House number not found in data base: " + houseNumber + "/" + flatNumber);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "House number not found in data base: " + houseNumber + "/" + flatNumber);
            });
            if (houseNumber == null || flatNumber == null) {
                logger.warn("House number: " + houseNumber + " or flat number: " + flatNumber + " is null");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "House number: " + houseNumber + " or flat number: " + flatNumber + " is null");
            }
            if (houseNumber < 0 || flatNumber < 0) {
                logger.warn("House number: " + houseNumber + " or flat number: " + flatNumber + " less then 0");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "House number: " + houseNumber + " or flat number: " + flatNumber + " less then 0");
            }
            logger.info("Getting information by house number and flat number: " + houseNumber + "/" + flatNumber);
            return userHouseMapper.toDto(userHouse);
        } catch (Exception e) {
            logger.error("Error getting information by house number and flat number: "
                    + houseNumber + "/" + flatNumber);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/town/{town}")
    public List<UserHouseDto> getHouseInTown(@PathVariable String town) {
        try {
            List<UserHouse> userHouses = userHouseService.getHouseInTown(town);
            if (userHouses.isEmpty()) {
                logger.warn("User House in this town not found in data base: " + town);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User House in this town not found in data base: " + town);
            }
            if (town != null && !town.matches("[A-Za-z\\-\\s]*$")) {
                logger.warn("Town: " + town + " has invalid characters");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Town: " + town + " is null");
            }
            logger.info("User house in this town retrieved from data base: " + town);
            return userHouses.
                    stream().
                    map(userHouseMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting information user house by town: " + town);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting information user house by town: " + e.getMessage());
        }
    }

    @GetMapping("/country/{country}")
    public List<UserHouseDto> getHouseInCountry(@PathVariable String country) {
        try {
            List<UserHouse> userHouses = userHouseService.getUserHousesInCountry(country);
            if (!Arrays.stream(SupportedCountries.values()).
                    map(Enum::name).
                    toList().
                    contains(country.toUpperCase())) {
                logger.warn("Unsupported country requested: " + country);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unsupported country requested: " + country);
            }
            if (userHouses.isEmpty()) {
                logger.warn("User house in this country not found in data base: " + country);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User house in this country not found in data base: " + country);
            }
            logger.info("User house in this country retrieved from data base: " + country);
            return userHouses.
                    stream().
                    map(userHouseMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting information user house by country: " + country);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting information user house by country: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public UserHouseDto addUserHouse(@RequestBody UserHouseDto userHouseDto) {
        try {
            if (userHouseDto.getUserName() != null && !userHouseDto.getUserName().matches("[a-zA-Z0-9_]+$")) {
                logger.warn("Invalid user name format: " + userHouseDto.getUserName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid user name format: " + userHouseDto.getUserName());
            }
            if (userHouseDto.getUserPhone() != null &&
                    !userHouseDto.getUserPhone().matches("^\\+(?:[0-9] ?){6,14}[0-9]$")) {
                logger.warn("Invalid phone number format: " + userHouseDto.getUserPhone());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid phone number format: " + userHouseDto.getUserPhone());
            }
            if (!Arrays.stream(SupportedCountries.values()).
                    map(Enum::name).
                    toList().
                    contains(userHouseDto.getCountry().toUpperCase())) {
                logger.warn("Unsupported country request: " + userHouseDto.getCountry());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unsupported country request: " + userHouseDto.getCountry());
            }
            if (userHouseDto.getTown() != null && !userHouseDto.getTown().matches("[A-Za-z\\-\\s]*$")) {
                logger.warn("Invalid town name characters: " + userHouseDto.getTown());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid town name characters: " + userHouseDto.getTown());
            }
            if (userHouseDto.getAddress() != null && !userHouseDto.getAddress().matches("^[A-Za-z-]+$")) {
                logger.warn("Invalid address name characters: " + userHouseDto.getAddress());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid address name characters: " + userHouseDto.getAddress());
            }
            if (userHouseDto.getHouseNumber() != null && !userHouseDto.getHouseNumber().toString().matches("\\d+")
                    && userHouseDto.getHouseNumber() < 0) {
                logger.warn("House number has invalid characters or less then 0: " + userHouseDto.getHouseNumber());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "House number has invalid characters or less then 0: " + userHouseDto.getHouseNumber());
            }
            if (userHouseDto.getFlatNumber() != null && !userHouseDto.getFlatNumber().toString().matches("\\d+")
                    && userHouseDto.getFlatNumber() < 0) {
                logger.warn("Flat number has invalid characters or less then 0: " + userHouseDto.getFlatNumber());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Flat number has invalid characters or less then 0: " + userHouseDto.getFlatNumber());
            }
            UserHouse userHouse = userHouseMapper.toEntity(userHouseDto);
            UserHouse userSave = userHouseService.save(userHouse);
            logger.info("New user created with id: " + userHouseDto.getId());
            return userHouseMapper.toDto(userSave);
        } catch (Exception e) {
            logger.error("Error save user house to data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error save user house to data base: " + e.getMessage());
        }
    }

    @PutMapping("/update/houseNumber/{houseNumber}/flatNumber/{flatNumber}")
    public UserHouseDto updateUserHouse(@PathVariable Integer houseNumber,
                                        @PathVariable Integer flatNumber, @RequestBody UserHouseDto userHouseDto) {
        try {
            Optional<UserHouse> userHouseOptional = userHouseService.
                    getInformationByHouseNumberAndFlatNumber(houseNumber, flatNumber);
            UserHouse userHouse = userHouseOptional.orElseThrow(() -> {
                logger.warn("House number: " + houseNumber + " or flat number: " + flatNumber +
                        " not found in data base");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "House number: " + houseNumber +
                        " or flat number: " + flatNumber +
                        " not found in data base");
            });
            if (houseNumber < 0 || flatNumber < 0) {
                logger.warn("House number: " + houseNumber + " or flat number: " + flatNumber + " less then 0");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "House number: " + houseNumber + " or flat number: " + flatNumber + " less then 0");
            }
            if (!userHouseDto.getUserName().matches("[a-zA-Z0-9_]+$")) {
                logger.warn("Invalid user name format: " + userHouseDto.getUserName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid user name format: " + userHouseDto.getUserName());
            }
            if (!userHouseDto.getUserPhone().matches("^\\+(?:[0-9] ?){6,14}[0-9]$")) {
                logger.warn("Invalid phone format: " + userHouseDto.getUserPhone());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid phone format: " + userHouseDto.getUserPhone());
            }
            if (!Arrays.stream(SupportedCountries.
                            values()).
                    map(Enum::name).
                    toList().
                    contains(userHouseDto.getCountry().toUpperCase())) {
                logger.warn("Unsupported country request to update: " + userHouseDto.getCountry());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unsupported country request to update: " + userHouseDto.getCountry());
            }
            UserHouse userHouseUpdate = userHouseService.updateUserHouse(userHouse, userHouseDto);
            logger.info("Updating information with house number and flat number: "
                    + houseNumber + "/" + flatNumber);
            return userHouseMapper.toDto(userHouseUpdate);
        } catch (Exception e) {
            logger.warn("Error updating user house: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating user house: " + e.getMessage());
        }
    }

    @DeleteMapping("/{houseNumber}/{flatNumber}")
    public String deleteUserHouse(@PathVariable Integer houseNumber, @PathVariable Integer flatNumber) {
        try {
            if (houseNumber < 0 && flatNumber < 0) {
                logger.warn("House number: " + houseNumber + " or flat number: " + flatNumber + " less then 0");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "House number: " + houseNumber + " or flat number: " + flatNumber + " less then 0");
            }
            Optional<UserHouse> userHouse = userHouseService.
                    getInformationByHouseNumberAndFlatNumber(houseNumber, flatNumber);
            if (userHouse.isEmpty()) {
                logger.warn("User house with house number: " + houseNumber + " or flat number: " + flatNumber
                        + " is empty");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User house with house number: "
                        + houseNumber + " or flat number: " + flatNumber + " is empty");
            }
            userHouseService.deleteUserHouse(userHouse.get());
            logger.info("User house with house number: " + houseNumber + " and flat number: " + flatNumber
                    + " was successful deleted");
            return "User house with house number: " + houseNumber + " and flat number: " + flatNumber
                    + " was successful deleted";
        } catch (Exception e) {
            logger.error("Error delete user house from data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error delete user house from data base: " + e.getMessage());
        }
    }
}
