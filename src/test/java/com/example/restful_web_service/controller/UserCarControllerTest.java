package com.example.restful_web_service.controller;

import com.example.restful_web_service.controller.dto.UserCarDto;
import com.example.restful_web_service.controller.mapper.UserCarMapper;
import com.example.restful_web_service.entity.UserCar;
import com.example.restful_web_service.service.UserCarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserCarControllerTest {

    private UserCarController userCarController;
    @Mock
    private UserCarService userCarService;
    @Mock
    private UserCarMapper userCarMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userCarController = new UserCarController(userCarMapper, userCarService);
    }

    @Test
    public void getAllInformationUserCar() {
        List<UserCar> userCars = new ArrayList<>();
        userCars.add(new UserCar(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223"));
        userCars.add(new UserCar(2, "Andrew", "andrew@gmail.com",
                "Toyota", 2005, "Land Cruises", "LM7657589723223"));
        when(userCarService.getAllInformationUserCar()).thenReturn(userCars);

        List<UserCarDto> userCarDto = new ArrayList<>();
        userCarDto.add(new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223"));
        userCarDto.add(new UserCarDto(2, "Andrew", "andrew@gmail.com",
                "Toyota", 2005, "Land Cruises", "LM7657589723223"));
        when(userCarMapper.toDto(userCars.get(0))).thenReturn(userCarDto.get(0));
        when(userCarMapper.toDto(userCars.get(1))).thenReturn(userCarDto.get(1));

        List<UserCarDto> response = userCarController.getAllInformationUserCar();

        assertEquals(userCarDto, response);
    }

    @Test
    public void getAllInformationUserCarEmptyList() {
        when(userCarService.getAllInformationUserCar()).thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getAllInformationUserCar());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserCarByVinCode() {
        UserCar userCar = new UserCar(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");
        when(userCarService.getUserCarByVinCode("LM7657689723223")).thenReturn(userCar);

        UserCarDto userCarDto = new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");
        when(userCarMapper.toDto(userCar)).thenReturn(userCarDto);

        UserCarDto response = userCarController.getUserCarByVinCode("LM7657689723223");

        assertEquals(userCarDto, response);
    }

    @Test
    public void getUserCarByVinCodeEmptyList() {
        String vinCode = "";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByVinCode(vinCode));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserCarByVinCodeInvalidCharacters() {
        String vinCode = "LM7657689723223@";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByVinCode(vinCode));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserCarByGraduationYear() {
        UserCar userCar = new UserCar(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");
        UserCarDto userCarDto = new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");

        when(userCarService.getUserCarByGraduationYear(2000)).thenReturn(Optional.of(userCar));
        when(userCarMapper.toDto(userCar)).thenReturn(userCarDto);

        UserCarDto actualUserCarDto = userCarController.getUserCarByGraduationYear(2000);

        assertEquals(userCarDto, actualUserCarDto);
    }

    @Test
    public void getUserCarByGraduationYearEmptyList() {
        when(userCarService.getUserCarByGraduationYear(2000)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByGraduationYear(2000));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserCarByGraduationYearInvalidCharacter() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByGraduationYear(20000));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserCarByUserNameAndEmail() {
        List<UserCar> userCars = new ArrayList<>();
        userCars.add(new UserCar(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223"));
        List<UserCarDto> userCarDto = new ArrayList<>();
        userCarDto.add(new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223"));

        when(userCarService.getUserByUserNameAndEmail("John", "john@gmail.com")).thenReturn(userCars);
        when(userCarMapper.toDto(userCars.get(0))).thenReturn(userCarDto.get(0));

        List<UserCarDto> response = userCarController.
                getUserCarByUserNameAndEmail("John", "john@gmail.com");

        assertEquals(userCarDto, response);
    }

    @Test
    public void getUserCarByUserNameAndEmailEmptyList() {
        when(userCarService.getUserByUserNameAndEmail("John", "john@gmail.com")).
                thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByUserNameAndEmail("John", "john@gmail.com"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserCarByUserNameAndEmailInvalidUserNameCharacter() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByUserNameAndEmail("John@", "john@gmail.com"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserCarByUserNameAndEmailInvalidEmailCharacter() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByUserNameAndEmail("John", "john!@gmail.com"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserCarByBrandCarAndModel() {
        List<UserCar> userCars = new ArrayList<>();
        userCars.add(new UserCar(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223"));
        List<UserCarDto> userCarDto = new ArrayList<>();
        userCarDto.add(new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223"));

        when(userCarService.getUserCarByBrandCarAndModel("Toyota", "Corolla")).thenReturn(userCars);
        when(userCarMapper.toDto(userCars.get(0))).thenReturn(userCarDto.get(0));

        List<UserCarDto> response = userCarController.getUserCarByBrandCarAndModel("Toyota", "Corolla");

        assertEquals(userCarDto, response);
    }

    @Test
    public void getUserCarByBrandCarAndModelEmptyList() {
        when(userCarService.getUserCarByBrandCarAndModel("Toyota", "Corolla")).
                thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByBrandCarAndModel("Toyota", "Corolla"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserByBrandCarAndModelInvalidBrandCarCharacter() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByBrandCarAndModel("Toyota@", "Corolla"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserByBrandCarAndModelInvalidModelCharacter() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.getUserCarByBrandCarAndModel("Toyota", "Corolla@"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void createUserCar() {
        UserCarDto userCarDto = new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");
        UserCar userCar = new UserCar(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");

        when(userCarMapper.toEntity(userCarDto)).thenReturn(userCar);
        when(userCarService.createUserCar(userCar)).thenReturn(userCar);
        when(userCarMapper.toDto(userCar)).thenReturn(userCarDto);

        UserCarDto result = userCarController.createUserCar(userCarDto);

        assertThat(result.getId()).isEqualTo(userCarDto.getId());
        assertThat(result.getUserName()).isEqualTo(userCarDto.getUserName());
        assertThat(result.getUserEmail()).isEqualTo(userCarDto.getUserEmail());
        assertThat(result.getBrandCar()).isEqualTo(userCarDto.getBrandCar());
        assertThat(result.getGraduationYear()).isEqualTo(userCarDto.getGraduationYear());
        assertThat(result.getModel()).isEqualTo(userCarDto.getModel());
        assertThat(result.getCarVinCode()).isEqualTo(userCarDto.getCarVinCode());
    }

    @Test
    public void createUserCarInvalidName() {
        UserCarDto userCarDto = new UserCarDto(1, "John@", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.createUserCar(userCarDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void createUserCarInvalidEmail() {
        UserCarDto userCarDto = new UserCarDto(1, "John", "john!@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.createUserCar(userCarDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void createUserCarInvalidBrandCar() {
        UserCarDto userCarDto = new UserCarDto(1, "John", "john@gmail.com",
                "Toyota@", 2000, "Corolla", "LM7657689723223");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.createUserCar(userCarDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void createUserCarInvalidGraduationYear() {
        UserCarDto userCarDto = new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 20000, "Corolla", "LM7657689723223");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.createUserCar(userCarDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void createUserCarInvalidModel() {
        UserCarDto userCarDto = new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla@", "LM7657689723223");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.createUserCar(userCarDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void createUserCarInvalidVinCode() {
        UserCarDto userCarDto = new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223@");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.createUserCar(userCarDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void updateUserCarInformation() {
        String vinCode = "LM7657689723223";
        UserCarDto userCarDto = new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");

        UserCar userCar = new UserCar(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");
        when(userCarService.updateUserCarInformation(vinCode, userCarDto)).thenReturn(userCar);

        UserCarDto response = userCarController.updateUserCarInformation(vinCode, userCarDto);

        assertEquals(userCarMapper.toDto(userCar), response);
    }

    @Test
    public void updateUserCarInformationInvalidCarVinCode() {
        String vinCode = "LM7657689723223@";
        UserCarDto userCarDto = new UserCarDto(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.updateUserCarInformation(vinCode, userCarDto));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void updateUserCarInformationInvalidFormat() {
        String vinCode = "LM7657689723223";
        UserCarDto userCarDto = new UserCarDto();
        userCarDto.setUserName("John@");
        userCarDto.setUserEmail("john!@gmail.com");
        userCarDto.setGraduationYear(20000);
        userCarDto.setModel("Corolla@");

        assertThrows(ResponseStatusException.class, () ->
                userCarController.updateUserCarInformation(vinCode, userCarDto));
    }

    @Test
    public void deleteUserCarInformation() {
        String vinCode = "LM7657689723223";
        UserCar userCar = new UserCar(1, "John", "john@gmail.com",
                "Toyota", 2000, "Corolla", "LM7657689723223");

        when(userCarService.getUserCarByVinCode(vinCode)).thenReturn(userCar);

        String expectedResponse = "User car with VIN Code: " + vinCode + " successful deleted";
        String actualResponse = userCarController.deleteUserCarInformation(vinCode);

        verify(userCarService, times(1)).deleteUserCarInformation(userCar);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void deleteUserCarInvalidVinCode() {
        String vinCode = "LM7657689723223@";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userCarController.deleteUserCarInformation(vinCode));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }
}