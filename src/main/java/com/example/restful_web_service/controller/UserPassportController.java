package com.example.restful_web_service.controller;

import com.example.restful_web_service.controller.dto.UserPassportDto;
import com.example.restful_web_service.controller.mapper.UserPassportMapper;
import com.example.restful_web_service.entity.UserPassport;
import com.example.restful_web_service.service.UserPassportService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/passport")
public class UserPassportController {

    private final UserPassportMapper userPassportMapper;
    private final UserPassportService userPassportService;
    private final Logger logger = Logger.getLogger(UserPassportController.class);

    @Autowired
    public UserPassportController(UserPassportMapper userPassportMapper, UserPassportService userPassportService) {
        this.userPassportMapper = userPassportMapper;
        this.userPassportService = userPassportService;
    }

    @GetMapping
    public List<UserPassportDto> getAllInformation() {
        try {
            List<UserPassport> userPassports = userPassportService.getAllInformation();
            if (userPassports.isEmpty()) {
                logger.warn("Passport details not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passport details not found");
            }
            logger.info("Passport information retrieved from data base");
            return userPassports.
                    stream().
                    map(userPassportMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving passport information from data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{passportNumber}")
    public UserPassportDto getPassportInformationByNumber(@PathVariable Integer passportNumber) {
        try {
            Optional<UserPassport> userOptional = userPassportService.getPassportInformationByNumber(passportNumber);
            UserPassport user = userOptional.orElseThrow(() -> {
                logger.warn("Passport number not found in data base: " + passportNumber);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Passport number not found in data base: " + passportNumber);
            });
            logger.info("Getting passport information by passport number: " + passportNumber);
            return userPassportMapper.toDto(user);
        } catch (Exception e) {
            logger.error("Error getting passport information by passport number from data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting passport information by passport number from data base: " + e.getMessage());
        }
    }

    @GetMapping("/nationality")
    public List<UserPassportDto> getPassportInformationByNationality(@RequestParam String nationality) {
        try {
            List<UserPassport> userPassports = userPassportService.getPassportInformationByNationality(nationality);
            if (userPassports.isEmpty()) {
                logger.warn("Passport details by nationality not found: " + nationality);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Passport details by nationality not found: " + nationality);
            }
            if (!nationality.matches("^[\\p{L} -]+$")) {
                logger.warn("Invalid parameter value: nationality contains invalid characters");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid parameter value: nationality contains invalid characters");
            }
            if (nationality.length() < 2 || nationality.length() > 90) {
                logger.warn("Your nationality lenght less 2 or more 90: " + nationality);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Your nationality lenght less 2 or more 90: " + nationality);
            }
            logger.info("Passport details by nationality retrieved from data base: " + nationality);
            return userPassports.
                    stream().
                    map(userPassportMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting passport information by nationality from data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting passport information by nationality from data base: " + e.getMessage());
        }
    }

    @GetMapping("/valid")
    public List<UserPassportDto> getValidPassport() {
        try {
            LocalDate localDate = LocalDate.now();
            List<UserPassport> userPassports = userPassportService.getAllInformation().stream().
                    filter(userPassport -> userPassport.getDateOfExpire() == null
                            || LocalDate.parse(userPassport.getDateOfExpire(),
                            DateTimeFormatter.ofPattern("dd.MM.yyyy")).isAfter(localDate)).toList();
            if (userPassports.isEmpty()) {
                logger.warn("Valid passport details not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Valid passport details not found");
            }
            logger.info("Valid passport information retrieved from data base");
            return userPassports.
                    stream().
                    map(userPassportMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting valid user passport from data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting valid user passport from data base: " + e.getMessage());
        }
    }

    @GetMapping("/expired")
    public List<UserPassportDto> getExpiredPassport() {
        try {
            LocalDate localDate = LocalDate.now();
            List<UserPassport> userPassports = userPassportService.getAllInformation().
                    stream().filter(userPassport -> userPassport.getDateOfExpire() != null
                            && LocalDate.parse(userPassport.getDateOfExpire(),
                            DateTimeFormatter.ofPattern("dd.MM.yyyy")).isBefore(localDate)).toList();
            if (userPassports.isEmpty()) {
                logger.warn("Expired passport details not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expired passport details not found");
            }
            logger.info("Expired passport retrieved from data base");
            return userPassports.
                    stream().
                    map(userPassportMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting expired user passport from database: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting expired user passport from database: " + e.getMessage());
        }
    }

    @PostMapping
    public UserPassportDto addUserPassport(@RequestBody UserPassportDto userPassportDto) {
        try {
            if(userPassportDto.getName() != null && !userPassportDto.getName().matches("[a-zA-Z\\-]+")) {
                logger.warn("Invalid name characters: " + userPassportDto.getName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid name characters: " + userPassportDto.getName());
            }
            if (userPassportDto.getSurname() != null && !userPassportDto.getSurname().matches("[a-zA-Z\\-]+")) {
                logger.warn("Invalid surname characters: " + userPassportDto.getSurname());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid surname characters: " + userPassportDto.getSurname());
            }
            if (!userPassportDto.getSex().equals("Male") && !userPassportDto.getSex().equals("Female")) {
                logger.warn("Gender not specified");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gender not specified");
            }
            if (userPassportDto.getDateOfBirth() != null &&
                    !userPassportDto.getDateOfBirth().matches("\\d{2}/\\d{2}/\\d{4}")) {
                logger.warn("Invalid date of birth characters: " + userPassportDto.getDateOfBirth());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid date of birth characters: " + userPassportDto.getDateOfBirth());
            }
            if (userPassportDto.getNationality() != null &&
                    !userPassportDto.getNationality().matches("^[\\p{L} -]+$")) {
                logger.warn("Invalid nationality characters: " + userPassportDto.getNationality());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid nationality characters: " + userPassportDto.getNationality());
            }
            if (userPassportDto.getDateOfIssue() != null &&
                    !userPassportDto.getDateOfIssue().matches("\\d{2}/\\d{2}/\\d{4}")) {
                logger.warn("Invalid date of issue characters: " + userPassportDto.getDateOfIssue());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid date of issue characters: " + userPassportDto.getDateOfIssue());
            }
            if (userPassportDto.getDateOfExpire() != null &&
                    !userPassportDto.getDateOfExpire().matches("\\d{2}/\\d{2}/\\d{4}")) {
                logger.warn("Invalid date of expire characters: " + userPassportDto.getDateOfExpire());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid date of expire characters: " + userPassportDto.getDateOfExpire());
            }
            if (userPassportDto.getPassportNumber() != null &&
                    !userPassportDto.getPassportNumber().toString().matches("\\d{9}")) {
                logger.warn("Passport number more or less than 9 or invalid passport number characters: "
                        + userPassportDto.getPassportNumber());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Passport number more or less than 9 or invalid passport number characters: "
                                + userPassportDto.getPassportNumber());
            }
            UserPassport userPassport = userPassportMapper.toEntity(userPassportDto);
            UserPassport userSave = userPassportService.save(userPassport);
            logger.info("New user created with id: " + userSave.getId());
            return userPassportMapper.toDto(userSave);
        } catch (Exception e) {
            logger.error("Error save user passport information to data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error save user passport information to data base: " + e.getMessage());
        }
    }

    @PutMapping("/{passportNumber}")
    public UserPassportDto updateUserPassport(@PathVariable Integer passportNumber,
                                              @RequestBody UserPassportDto userPassportDto) {
        try {
            Optional<UserPassport> userPassportOptional = userPassportService.
                    getPassportInformationByNumber(passportNumber);
            UserPassport userPassportByNumber = userPassportOptional.orElseThrow(() -> {
                logger.warn("Passport user with this passport number: " + passportNumber + " not found");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Passport user with this passport number: " + passportNumber + " not found");
            });
            UserPassport userPassportUpdate = userPassportService.
                    updateUserPassport(userPassportByNumber, userPassportDto);
            logger.info("User with passport number: " + passportNumber + " updated");
            return userPassportMapper.toDto(userPassportUpdate);
        } catch (Exception e) {
            logger.error("Error update user passport to data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error update user passport to data base: " + e.getMessage());
        }
    }

    @DeleteMapping
    public String delete(@RequestParam Integer passportNumber) {
        try {
            if (passportNumber.toString().length() != 9) {
                logger.warn("Error deleting user passport with passport number less or more 9");
                return "Error deleting user passport with passport number less or more 9";
            }
            Optional<UserPassport> userPassport = userPassportService.getPassportInformationByNumber(passportNumber);
            if (userPassport.isEmpty()) {
                logger.warn("User passport with passport number: " + passportNumber + " not found in data base");
                return "User passport with passport number: " + passportNumber + " not found in data base";
            }
            userPassportService.delete(userPassport.get());
            logger.info("User passport with passport number: " + passportNumber + " was successfully deleted");
            return "User passport with passport number: " + passportNumber + " was successfully deleted";
        } catch (Exception e) {
            logger.error("Error delete user passport from data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error delete user passport from data base: " + e.getMessage());
        }
    }
}
