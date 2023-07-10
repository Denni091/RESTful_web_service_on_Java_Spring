package com.example.restful_web_service.controller;

import com.example.restful_web_service.controller.dto.UserHouseDto;
import com.example.restful_web_service.controller.enums.SupportedCountries;
import com.example.restful_web_service.controller.mapper.UserHouseMapper;
import com.example.restful_web_service.entity.UserHouse;
import com.example.restful_web_service.service.UserHouseService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserHouseControllerTest {

    private UserHouseController userHouseController;
    @Mock
    private UserHouseService userHouseService;
    @Mock
    private UserHouseMapper userHouseMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userHouseController = new UserHouseController(userHouseService, userHouseMapper);
    }

    @Test
    public void getAllInformationUserHouse() {
        List<UserHouse> userHouses = new ArrayList<>();
        userHouses.add(new UserHouse(1, "John", "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12));
        userHouses.add(new UserHouse(1, "John", "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12));

        when(userHouseService.getAllInformationUserHouse()).thenReturn(userHouses);

        List<UserHouseDto> userHouseDto = new ArrayList<>();
        userHouseDto.add(new UserHouseDto(1, "John", "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12));
        userHouseDto.add(new UserHouseDto(1, "John", "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12));

        when(userHouseMapper.toDto(userHouses.get(0))).thenReturn(userHouseDto.get(0));
        when(userHouseMapper.toDto(userHouses.get(1))).thenReturn(userHouseDto.get(1));

        List<UserHouseDto> response = userHouseController.getAllInformationUserHouse();

        assertEquals(userHouseDto, response);
    }

    @Test
    public void getAllInformationEmptyList() {
        when(userHouseService.getAllInformationUserHouse()).thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.getAllInformationUserHouse());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getInformationByHouseNumberAndFlatNumber() {
        UserHouse userHouse = new UserHouse(1, "John", "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);
        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);

        when(userHouseService.getInformationByHouseNumberAndFlatNumber(25, 12)).
                thenReturn(Optional.of(userHouse));
        when(userHouseMapper.toDto(userHouse)).thenReturn(userHouseDto);

        UserHouseDto actualUserDto = userHouseController.
                getInformationByHouseNumberAndFlatNumber(25, 12);

        assertEquals(userHouseDto, actualUserDto);
    }

    @Test
    public void getInformationByHouseNumberAndFlatNumberDoesNotExists() {
        when(userHouseService.getInformationByHouseNumberAndFlatNumber(25, 12)).
                thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.getInformationByHouseNumberAndFlatNumber(25, 12));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getInformationByHouseAndFlatNumberIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.getInformationByHouseNumberAndFlatNumber(null, null));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getInformationByHouseAndFlatNumberIsLessThan0() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.getInformationByHouseNumberAndFlatNumber(-1, -1));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getHouseInTown() {
        List<UserHouse> userHouses = new ArrayList<>();
        userHouses.add(new UserHouse(1, "John", "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12));
        when(userHouseService.getHouseInTown("Lissabon")).thenReturn(userHouses);

        List<UserHouseDto> userHouseDto = new ArrayList<>();
        userHouseDto.add(new UserHouseDto(1, "John", "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12));
        when(userHouseMapper.toDto(userHouses.get(0))).thenReturn(userHouseDto.get(0));

        List<UserHouseDto> response = userHouseController.getHouseInTown("Lissabon");

        assertEquals(userHouseDto, response);
    }

    @Test
    public void getHouseInTownEmptyList() {
        when(userHouseService.getHouseInTown("Lissabon")).thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.getHouseInTown("Lissabon"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getHouseInTownInvalidCharacters() {
        String town = "Lissabon";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.getHouseInTown(town));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getHouseInCountry() {
        String country = SupportedCountries.PORTUGAL.name();

        List<UserHouse> userHouses = new ArrayList<>();
        userHouses.add(new UserHouse(1, "John", "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12));
        when(userHouseService.getUserHousesInCountry(country)).thenReturn(userHouses);

        List<UserHouseDto> userHouseDto = new ArrayList<>();
        userHouseDto.add(new UserHouseDto(1, "John", "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12));

        when(userHouseMapper.toDto(userHouses.get(0))).thenReturn(userHouseDto.get(0));

        List<UserHouseDto> response = userHouseController.getHouseInCountry(country);

        assertEquals(userHouseDto, response);
    }

    @Test
    public void getHouseInCountryUnsupportedCountry() {
        String country = "USA";

        when(userHouseService.getUserHousesInCountry(country)).thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.getHouseInCountry(country));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Unsupported country requested"));
    }

    @Test
    public void getHouseInCountryWithNoUserHouses() {
        String country = SupportedCountries.POLAND.name();

        when(userHouseService.getUserHousesInCountry(country)).thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.getHouseInCountry(country));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("User house in this country not found in data base"));
    }

    @Test
    public void addUserHouse() {
        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);
        UserHouse userHouse = new UserHouse(1, "John",
                "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);

        when(userHouseMapper.toEntity(userHouseDto)).thenReturn(userHouse);
        when(userHouseService.save(userHouse)).thenReturn(userHouse);
        when(userHouseMapper.toDto(userHouse)).thenReturn(userHouseDto);

        UserHouseDto result = userHouseController.addUserHouse(userHouseDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userHouseDto.getId());
        assertThat(result.getUserName()).isEqualTo(userHouseDto.getUserName());
        assertThat(result.getUserPhone()).isEqualTo(userHouseDto.getUserPhone());
        assertThat(result.getCountry()).isEqualTo(userHouseDto.getCountry());
        assertThat(result.getTown()).isEqualTo(userHouseDto.getTown());
        assertThat(result.getAddress()).isEqualTo(userHouseDto.getAddress());
        assertThat(result.getHouseNumber()).isEqualTo(userHouseDto.getHouseNumber());
        assertThat(result.getFlatNumber()).isEqualTo(userHouseDto.getFlatNumber());

        verify(userHouseMapper).toEntity(userHouseDto);
        verify(userHouseService).save(userHouse);
        verify(userHouseMapper).toDto(userHouse);
    }

    @Test
    public void addUserHouseInvalidName() {
        UserHouseDto userHouseDto = new UserHouseDto(1, "John%",
                "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.addUserHouse(userHouseDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserHouseInvalidUserPhone() {
        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.addUserHouse(userHouseDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserHouseUnsupportedCountry() {
        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "+380978657654", "USA",
                "Lissabon", "lissabon", 25, 12);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.addUserHouse(userHouseDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserHouseInvalidTown() {
        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "+380978657654", "Portugal",
                "Lissabon123", "lissabon", 25, 12);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.addUserHouse(userHouseDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidHouseNumber() {
        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "+380978657654", "Portugal",
                "Lissabon123", "lissabon", -1, 12);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.addUserHouse(userHouseDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidFlatNumber() {
        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "+380978657654", "Portugal",
                "Lissabon123", "lissabon", 25, -12);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.addUserHouse(userHouseDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void updateUserHouse() {
        Integer houseNumber = 25;
        Integer flatNumber = 12;

        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);
        UserHouse userHouse = new UserHouse(1, "John",
                "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);
        UserHouse userHouseUpdate = new UserHouse(1, "Denis",
                "+380978657554", "Ukraine",
                "Kyiv", "Chreshatik", 21, 11);

        when(userHouseService.getInformationByHouseNumberAndFlatNumber(houseNumber, flatNumber)).
                thenReturn(Optional.of(userHouse));
        when(userHouseService.updateUserHouse(userHouse, userHouseDto)).thenReturn(userHouseUpdate);
        when(userHouseMapper.toDto(userHouseUpdate)).thenReturn(userHouseDto);

        UserHouseDto updatedUserHouseDto = userHouseController.updateUserHouse(houseNumber, flatNumber, userHouseDto);

        assertEquals(userHouseDto, updatedUserHouseDto);
    }

    @Test
    public void updateUserHouseDoesNotFound() {
        Integer houseNumber = 25;
        Integer flatNumber = 12;

        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);
        when(userHouseService.getInformationByHouseNumberAndFlatNumber(houseNumber, flatNumber)).
                thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.updateUserHouse(houseNumber, flatNumber, userHouseDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void updateUserHouseHoseNumberAndFlatNumberLessThan0() {
        Integer houseNumber = -1;
        Integer flatNumber = -1;
        UserHouseDto userHouseDto = new UserHouseDto(1, "John",
                "+380978657654", "Portugal",
                "Lissabon", "lissabon", houseNumber, flatNumber);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.updateUserHouse(houseNumber, flatNumber, userHouseDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void updateUserHouseWithInvalidMethodFormat() {
        Integer houseNumber = 25;
        Integer flatNumber = 12;

        UserHouseDto userHouseDto = new UserHouseDto();
        userHouseDto.setUserName("user_name@");
        userHouseDto.setUserPhone("380976542676");
        userHouseDto.setCountry("USA");

        assertThrows(ResponseStatusException.class, () ->
                userHouseController.updateUserHouse(houseNumber, flatNumber, userHouseDto));
    }

    @Test
    public void deleteUserHouse() {
        Integer houseNumber = 25;
        Integer flatNumber = 12;

        UserHouse userHouse = new UserHouse(1, "John",
                "+380978657654", "Portugal",
                "Lissabon", "lissabon", 25, 12);

        when(userHouseService.getInformationByHouseNumberAndFlatNumber(houseNumber, flatNumber)).
                thenReturn(Optional.of(userHouse));

        String expectedResponse = "User house with house number: " + houseNumber + " and flat number: " + flatNumber
                + " was successful deleted";
        String actualResponse = userHouseController.deleteUserHouse(houseNumber, flatNumber);

        verify(userHouseService, times(1)).deleteUserHouse(userHouse);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void deleteUserHouseLessThen0() {
        Integer houseNumber = -1;
        Integer flatNumber = -1;
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.deleteUserHouse(houseNumber, flatNumber));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void deleteUserHouseEmptyList() {
        List<UserHouse> userHouses = new ArrayList<>();
        when(userHouseService.getAllInformationUserHouse()).thenReturn(userHouses);

        Integer houseNumber = 1;
        Integer flatNumber = 2;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userHouseController.deleteUserHouse(houseNumber, flatNumber));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
     }
}