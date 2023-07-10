package com.example.restful_web_service.controller;

import com.example.restful_web_service.controller.dto.UserDto;
import com.example.restful_web_service.controller.mapper.UserMapper;
import com.example.restful_web_service.entity.User;
import com.example.restful_web_service.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserDto> getAllUser() {
        try {
            List<User> userList = userService.getAllUser();
            if (userList.isEmpty()) {
                logger.warn("No users found in the Data Base");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Users not found");
            }
            logger.info("All users retrieved from the database.");
            return userList.
                    stream().
                    map(userMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting all user: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting all user: " + e.getMessage());
        }
    }

    @GetMapping("/sort")
    public List<UserDto> getAllUsersSorted() {
        try {
            List<User> userList = userService.getAllUsersSorted();
            if (userList.isEmpty()) {
                logger.warn("No users found in Data Base");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Users not found");
            }
            logger.info("All users retrieved from the database.");
            return userList.
                    stream().
                    map(userMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting information from data base: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting information from data base: " + e.getMessage());
        }
    }

    @GetMapping("/count")
    public Long getUserCount() {
        try {
            Long count = userService.getUserCount();
            if (count == 0) {
                logger.warn("No users found in the Data Base");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Users not found");
            }
            logger.info("Number of users in the database: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting information by user count: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Integer id) {
        try {
            Optional<User> optionalUser = userService.getUserById(id);
            User userById = optionalUser.orElseThrow(() -> {
                logger.warn("User with this id: " + id + " not found");
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id: " + id + " not found");
            });
            logger.info("Getting user with id: " + id);
            return userMapper.toDto(userById);
        } catch (Exception e) {
            logger.error("Error getting information from id: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<UserDto> getUserByFilter(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) Integer age,
                                         @RequestParam(required = false) String email,
                                         @RequestParam(required = false) String phone) {
        try {
            List<User> usersByFilter = userService.getUsersByFilter(name, age, email, phone);
            if (name != null && !name.matches("[a-zA-Z\\-]+")) {
                logger.warn("Invalid name characters: " + name);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name characters: " + name);
            }
            if (age != null && !age.toString().matches("[0-9]+")) {
                logger.warn("Invalid age characters: " + age);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid age characters: " + age);
            }
            if (age != null && age <= 0 || age != null && age >= 120) {
                logger.warn("Age entered incorrectly: " + age);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age entered incorrectly: " + age);
            }
            if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                logger.warn("Invalid email characters: " + email);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email characters: " + email);
            }
            if (phone != null && !phone.matches("^\\+(?:[0-9] ?){6,14}[0-9]$")) {
                logger.warn("Invalid phone number characters: " + phone);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid phone number characters: " + phone);
            }
            logger.info("Getting user by filter: " + "\nName: " + name + " age: " + age +
                    " email: " + email + " phone: " + phone);
            return usersByFilter.
                    stream().
                    map(userMapper::toDto).
                    collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting information by filter: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error getting information by filter: " + e.getMessage());
        }
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        try {
            if (userDto.getName() != null && !userDto.getName().matches("[a-zA-Z\\-]+")) {
                logger.warn("Invalid name characters: " + userDto.getName());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid name characters: " + userDto.getName());
            }
            if (userDto.getAge() != null && !userDto.getAge().toString().matches("[0-9]+")) {
                logger.warn("Invalid age characters: " + userDto.getAge());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid age characters: " + userDto.getAge());
            }
            if (userDto.getAge() != null && userDto.getAge() <= 0 || userDto.getAge() != null
                    && userDto.getAge() >= 120) {
                logger.warn("Age entered incorrectly: " + userDto.getAge());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Age entered incorrectly: " + userDto.getAge());
            }
            if (userDto.getEmail() != null && !userDto.getEmail().
                    matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                logger.warn("Invalid email characters: " + userDto.getEmail());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid email characters: " + userDto.getEmail());
            }
            if (userDto.getPhone() != null && !userDto.getPhone().matches("^\\+(?:[0-9] ?){6,14}[0-9]$")) {
                logger.warn("Invalid phone number characters: " + userDto.getPhone());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid phone number characters: " + userDto.getPhone());
            }
            User user = userMapper.toEntity(userDto);
            User savedUser = userService.saveUser(user);
            logger.info("New user created with id: " + savedUser.getId());
            return userMapper.toDto(savedUser);
        } catch (Exception e) {
            logger.error("Error adding information: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error adding information: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        try {
            Optional<User> optionalUser = userService.getUserById(id);
            User userById = optionalUser.orElseThrow(() -> {
                logger.warn("User with this id for update: " + id + " not found");
                return new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with this id for update: " + id + " not found");
            });
            User updateUser = userService.updateUser(userById, userDto);
            logger.info("User with id: " + id + " updated");
            return userMapper.toDto(updateUser);
        } catch (Exception e) {
            logger.error("Error updating user information: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating user information: " + e.getMessage());
        }
    }

    @DeleteMapping
    public String delete(@RequestParam Integer id) {
        try {
            if (id == null || id < 0) {
                logger.error("Error deleting user with id: null");
                return "Error deleting user with id: null";
            }
            Optional<User> user = userService.getUserById(id);
            if (user.isEmpty()) {
                logger.error("User with id" + id + " not found");
                return "User with id " + id + " not found";
            }
            userService.delete(user.get());
            logger.info("User with id: " + id + " was successfully deleted");
            return "User with id " + id + " was successfully deleted";
        } catch (Exception e) {
            logger.error("Error deleting user information: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting user information: " + e.getMessage());
        }
    }
}
