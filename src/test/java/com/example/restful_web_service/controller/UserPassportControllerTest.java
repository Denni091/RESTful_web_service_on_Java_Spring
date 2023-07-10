package com.example.restful_web_service.controller;

import com.example.restful_web_service.controller.dto.UserPassportDto;
import com.example.restful_web_service.controller.mapper.UserPassportMapper;
import com.example.restful_web_service.entity.UserPassport;
import com.example.restful_web_service.service.UserPassportService;
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
class UserPassportControllerTest {

    private UserPassportController userPassportController;
    @Mock
    private UserPassportService userPassportService;
    @Mock
    private UserPassportMapper userPassportMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userPassportController = new UserPassportController(userPassportMapper, userPassportService);
    }

    @Test
    public void getAllInformation() {
        List<UserPassport> userPassports = new ArrayList<>();
        userPassports.add(new UserPassport(1, "John", "Ken", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789));
        userPassports.add(new UserPassport(2, "Max", "Ken", "Male",
                "15/01/1986", "Ukrainian",
                "12/02/2022", "12/02/2032", 123456789));
        when(userPassportService.getAllInformation()).thenReturn(userPassports);

        List<UserPassportDto> userDtoList = new ArrayList<>();
        userDtoList.add(new UserPassportDto(1, "John", "Ken", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789));
        userDtoList.add(new UserPassportDto(2, "Max", "Ken", "Male",
                "15.01.1986", "Ukrainian",
                "12/02/2022", "12/02/2032", 123456789));

        when(userPassportMapper.toDto(userPassports.get(0))).thenReturn(userDtoList.get(0));
        when(userPassportMapper.toDto(userPassports.get(1))).thenReturn(userDtoList.get(1));

        List<UserPassportDto> response = userPassportController.getAllInformation();
        assertEquals(userDtoList, response);
    }

    @Test
    public void getAllInformationNotFound() {
        when(userPassportService.getAllInformation()).thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.getAllInformation());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getPassportInformationByNumber() {
        UserPassport userPassport = new UserPassport(1, "John", "Ken", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789);
        UserPassportDto userPassportDto = new UserPassportDto(1, "John", "Ken", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789);
        when(userPassportService.getPassportInformationByNumber(123456789)).
                thenReturn(Optional.of(userPassport));
        when(userPassportMapper.toDto(userPassport)).thenReturn(userPassportDto);

        UserPassportDto actualUserDto = userPassportController.getPassportInformationByNumber(123456789);
        assertEquals(userPassportDto, actualUserDto);
    }

    @Test
    public void getPassportInformationByNumberDoesNotExists() {
        when(userPassportService.getPassportInformationByNumber(123456789)).
                thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.getPassportInformationByNumber(123456789));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getPassportInformationByNationality() {
        List<UserPassport> userPassports = new ArrayList<>();
        userPassports.add(new UserPassport(1, "John", "Ken", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789));
        when(userPassportService.getPassportInformationByNationality("American")).thenReturn(userPassports);

        List<UserPassportDto> userPassportDto = new ArrayList<>();
        userPassportDto.add(new UserPassportDto(1, "John", "Ken", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789));
        when(userPassportMapper.toDto(userPassports.get(0))).thenReturn(userPassportDto.get(0));

        List<UserPassportDto> response = userPassportController.getPassportInformationByNationality("American");
        assertEquals(userPassportDto, response);
    }

    @Test
    public void getPassportInformationByNationalityNotFound() {
        when(userPassportService.getPassportInformationByNationality("American")).thenReturn(Collections.emptyList());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.getPassportInformationByNationality("American"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getPassportInformationByNationalityWithInvalidCharacters() {
        String invalidNationality = "American123";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.getPassportInformationByNationality(invalidNationality));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getPassportInformationByNationalityWithLenghtLessThen2() {
        String nationality = "ll";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.getPassportInformationByNationality(nationality));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getValidPassport() {
        UserPassport expiredPassport = new UserPassport();
        expiredPassport.setDateOfExpire("01.01.2020");

        UserPassport validPassport = new UserPassport();
        validPassport.setDateOfExpire("01.01.2024");

        List<UserPassport> passports = new ArrayList<>();
        passports.add(expiredPassport);
        passports.add(validPassport);

        when(userPassportService.getAllInformation()).thenReturn(passports);

        List<UserPassportDto> result = userPassportController.getValidPassport();

        assertEquals(1, result.size());
        assertEquals(userPassportMapper.toDto(validPassport), result.get(0));
    }

    @Test
    public void getValidPassportReturnsEmptyList() {
        when(userPassportService.getAllInformation()).thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.getValidPassport());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getExpiredPassport() {
        UserPassport expiredPassport = new UserPassport();
        expiredPassport.setDateOfExpire("01.01.2020");

        UserPassport validPassport = new UserPassport();
        validPassport.setDateOfExpire("01.01.2024");

        List<UserPassport> passports = new ArrayList<>();

        passports.add(expiredPassport);
        passports.add(validPassport);

        when(userPassportService.getAllInformation()).thenReturn(passports);

        List<UserPassportDto> result = userPassportController.getExpiredPassport();

        assertEquals(1, result.size());
        assertEquals(userPassportMapper.toDto(expiredPassport), result.get(0));
    }

    @Test
    public void getExpiredPassportIsEmpty() {
        when(userPassportService.getAllInformation()).thenReturn(Collections.emptyList());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.getExpiredPassport());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserPassport() {
        UserPassportDto userPassportDto = new UserPassportDto(1, "John", "Ken", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789);
        UserPassport userPassport = new UserPassport(1, "John", "Ken", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789);

        when(userPassportMapper.toEntity(userPassportDto)).thenReturn(userPassport);
        when(userPassportService.save(userPassport)).thenReturn(userPassport);
        when(userPassportMapper.toDto(userPassport)).thenReturn(userPassportDto);

        UserPassportDto result = userPassportController.addUserPassport(userPassportDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userPassportDto.getId());
        assertThat(result.getName()).isEqualTo(userPassportDto.getName());
        assertThat(result.getSurname()).isEqualTo(userPassportDto.getSurname());
        assertThat(result.getSex()).isEqualTo(userPassportDto.getSex());
        assertThat(result.getDateOfBirth()).isEqualTo(userPassportDto.getDateOfBirth());
        assertThat(result.getNationality()).isEqualTo(userPassportDto.getNationality());
        assertThat(result.getDateOfIssue()).isEqualTo(userPassportDto.getDateOfIssue());
        assertThat(result.getDateOfExpire()).isEqualTo(userPassportDto.getDateOfExpire());
        assertThat(result.getPassportNumber()).isEqualTo(userPassportDto.getPassportNumber());

        verify(userPassportMapper).toEntity(userPassportDto);
        verify(userPassportService).save(userPassport);
        verify(userPassportMapper).toDto(userPassport);
    }

    @Test
    public void addUserInvalidName() {
        UserPassportDto userPassport = new UserPassportDto(1, "John123", "Ken", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.addUserPassport(userPassport));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidSurname() {
        UserPassportDto userPassport = new UserPassportDto(1, "John", "Ken123", "Male",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.addUserPassport(userPassport));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidSex() {
        UserPassportDto userPassport = new UserPassportDto(1, "John", "Ken", "Male123",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 123456789);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.addUserPassport(userPassport));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidDateOfBirth() {
        UserPassportDto userPassport = new UserPassportDto(1, "John", "Ken", "Male123",
                "15.01.1985", "American",
                "12/01/2022", "12/01/2032", 123456789);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.addUserPassport(userPassport));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidNationality() {
        UserPassportDto userPassport = new UserPassportDto(1, "John", "Ken", "Male123",
                "15/01/1985", "American123",
                "12/01/2022", "12/01/2032", 123456789);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.addUserPassport(userPassport));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidDateOfIssue() {
        UserPassportDto userPassport = new UserPassportDto(1, "John", "Ken", "Male123",
                "15/01/1985", "American",
                "12.01.2022", "12/01/2032", 123456789);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.addUserPassport(userPassport));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidDateOfExpire() {
        UserPassportDto userPassport = new UserPassportDto(1, "John", "Ken", "Male123",
                "15/01/1985", "American",
                "12/01/2022", "12.01.2032", 123456789);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.addUserPassport(userPassport));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidPassportNumber() {
        UserPassportDto userPassport = new UserPassportDto(1, "John", "Ken", "Male123",
                "15/01/1985", "American",
                "12/01/2022", "12/01/2032", 12345678);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.addUserPassport(userPassport));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }
    @Test
    public void updateUserPassport() {
        Integer passportNumber = 123456789;
        UserPassportDto userPassportDto = new UserPassportDto(1, "John", "Ken", "Male",
                "15.01.1985", "American",
                "12/01/2022", "12/01/2032", 123456789);

        UserPassport userPassport = new UserPassport(1, "John", "Ken", "Male",
                "15.01.1985", "American",
                "12/01/2022", "12/01/2032", passportNumber);

        UserPassport userPassportUpdated = new UserPassport(1, "Denis", "Den", "Male",
                "15.02.1985", "Ukrainian",
                "12/02/2022", "12/02/2032", passportNumber);

        when(userPassportService.getPassportInformationByNumber(passportNumber))
                .thenReturn(Optional.of(userPassport));
        when(userPassportService.updateUserPassport(userPassport, userPassportDto))
                .thenReturn(userPassportUpdated);
        when(userPassportMapper.toDto(userPassportUpdated)).thenReturn(userPassportDto);

        UserPassportDto updatedUserPassportDto = userPassportController.
                updateUserPassport(passportNumber, userPassportDto);

        assertEquals(userPassportDto, updatedUserPassportDto);
    }

    @Test
    public void updateUserNotFound() {
        Integer passportNumber = 123456789;
        UserPassportDto userPassportDto = new UserPassportDto(1, "John", "Ken", "Male",
                "15.01.1985", "American",
                "12/01/2022", "12/01/2032", 123456789);

        when(userPassportService.getPassportInformationByNumber(passportNumber)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userPassportController.updateUserPassport(passportNumber, userPassportDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void delete() {
        Integer passportNumber = 123456789;
        UserPassport userPassport = new UserPassport(1, "John", "Ken", "Male",
                "15.01.1985", "American",
                "12/01/2022", "12/01/2032", passportNumber);

        when(userPassportService.getPassportInformationByNumber(passportNumber)).
                thenReturn(Optional.of(userPassport));

        String expectedResponse = "User passport with passport number: "
                + passportNumber + " was successfully deleted";
        String actualResponse = userPassportController.delete(passportNumber);

        verify(userPassportService, times(1)).delete(userPassport);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void deletePassportNumberIsLessOrMoreNineLenght() {
        Integer passportNumber = 12345678;
        String expectedErrorMessage = "Error deleting user passport with passport number less or more 9";
        assertEquals(expectedErrorMessage, userPassportController.delete(passportNumber));
    }

    @Test
    public void deletePassportInformationByNumberNotFound() {
        Integer passportNumber = 123456789;
        when(userPassportService.getPassportInformationByNumber(passportNumber)).thenReturn(Optional.empty());

        String response = userPassportController.delete(passportNumber);

        assertEquals("User passport with passport number: "
                + passportNumber + " not found in data base", response);
    }
}
