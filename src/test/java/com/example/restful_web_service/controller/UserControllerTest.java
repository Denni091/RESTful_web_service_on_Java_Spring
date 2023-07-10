package com.example.restful_web_service.controller;

import com.example.restful_web_service.controller.dto.UserDto;
import com.example.restful_web_service.controller.mapper.UserMapper;
import com.example.restful_web_service.entity.User;
import com.example.restful_web_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, userMapper);
    }

    @Test
    public void getAllUser() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "John", 25, "john@gmail.com", "+0977035432"));
        userList.add(new User(2, "Max", 27, "max@gmail.com", "+0971035432"));
        when(userService.getAllUser()).thenReturn(userList);

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(new UserDto(1, "John", 25, "john@gmail.com", "+0977035432"));
        userDtoList.add(new UserDto(2, "Max", 27, "max@gmail.com", "+0971035432"));
        when(userMapper.toDto(userList.get(0))).thenReturn(userDtoList.get(0));
        when(userMapper.toDto(userList.get(1))).thenReturn(userDtoList.get(1));

        List<UserDto> response = userController.getAllUser();
        assertEquals(userDtoList, response);
    }

    @Test
    public void getAllUserNotFound() {
        when(userService.getAllUser()).thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.getAllUser());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getAllUserSorted() {
        User user = new User(1, "John", 25, "john@gmail.com", "+0977035432");
        User user1 = new User(2, "Max", 27, "max@gmail.com", "+0971035432");
        User user2 = new User(3, "Den", 21, "den@gmail.com", "+0977654321");

        List<User> userList = Arrays.asList(user, user1, user2);

        UserDto userDto = new UserDto(1, "John", 25, "john@gmail.com", "+0977035432");
        UserDto userDto1 = new UserDto(2, "Max", 27, "max@gmail.com", "+0971035432");
        UserDto userDto2 = new UserDto(3, "Den", 21, "den@gmail.com", "+0977654321");
        List<UserDto> userDtoList = Arrays.asList(userDto, userDto1, userDto2);

        when(userService.getAllUsersSorted()).thenReturn(userList);
        when(userMapper.toDto(userList.get(0))).thenReturn(userDtoList.get(0));
        when(userMapper.toDto(userList.get(1))).thenReturn(userDtoList.get(1));
        when(userMapper.toDto(userList.get(2))).thenReturn(userDtoList.get(2));

        List<UserDto> result = userController.getAllUsersSorted();

        assertThat(result).isEqualTo(userDtoList);
    }

    @Test
    public void getAllUserSortedNotFound() {
        when(userService.getAllUsersSorted()).thenReturn(Collections.emptyList());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.getAllUsersSorted());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserCount() {
        Long expectedCount = 5L;
        when(userService.getUserCount()).thenReturn(expectedCount);

        Long actualCount = userController.getUserCount();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void getUserCountNotFound() {
        Long userCount = 0L;
        when(userService.getUserCount()).thenReturn(userCount);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.getUserCount());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserById() {
        User user = new User(1, "John", 25, "john@gmail.com", "+0977035432");
        UserDto expectedUserDto =
                new UserDto(1, "John", 25, "john@gmail.com", "+0977035432");

        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expectedUserDto);

        UserDto actualUserDto = userController.getUserById(1);
        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    public void getUserByIdDoesNotExist() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.getUserById(1));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserByFilter() {
        List<User> usersByFilter = new ArrayList<>();
        usersByFilter.add(new User(1, "John", 25, "john@gmail.com", "+380977035432"));
        usersByFilter.add(new User(2, "Max", 27, "max@gmail.com", "+380971035432"));

        when(userService.getUsersByFilter("John", 25, null, null)).thenReturn(usersByFilter);

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(new UserDto(1, "John", 25, "john@gmail.com", "+380977035432"));
        userDtoList.add(new UserDto(2, "Max", 27, "max@gmail.com", "+380971035432"));
        when(userMapper.toDto(usersByFilter.get(0))).thenReturn(userDtoList.get(0));
        when(userMapper.toDto(usersByFilter.get(1))).thenReturn(userDtoList.get(1));

        List<UserDto> response = userController.getUserByFilter("John", 25, null, null);

        assertThat(response).isEqualTo(userDtoList);
        verify(userService).getUsersByFilter("John", 25, null, null);
    }

    @Test
    public void getUserByFilterInvalidName() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.getUserByFilter("J0hn", 15, "john@gmail.com", "+380977035432"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserByFilterInvalidAge() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.getUserByFilter("John", 150, "john@gmail.com", "+380977035432"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserByFilterInvalidEmail() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.getUserByFilter("John", 25, "john@!gmail.com", "+380977035432"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void getUserByFilterInvalidPhone() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.getUserByFilter("John", 25, "john@gmail.com", "380977035432"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUser() {
        UserDto userDto = new UserDto(1, "John", 25, "john@gmail.com", "+0977035432");
        User user = new User(1, "John", 25, "john@gmail.com", "+0977035432");

        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userService.saveUser(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userController.addUser(userDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getAge()).isEqualTo(userDto.getAge());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(result.getPhone()).isEqualTo(userDto.getPhone());

        verify(userMapper).toEntity(userDto);
        verify(userService).saveUser(user);
        verify(userMapper).toDto(user);
    }

    @Test
    public void addUserInvalidName() {
        UserDto userDto = new UserDto(1, "John123", 25, "john@gmail.com", "+0977035432");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.addUser(userDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidAge() {
        UserDto userDto = new UserDto(1, "John", 150, "john@gmail.com", "+0977035432");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.addUser(userDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidEmail() {
        UserDto userDto = new UserDto(1, "John", 25, "john@!gmail.com", "+0977035432");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.addUser(userDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void addUserInvalidPhone() {
        UserDto userDto = new UserDto(1, "John", 25, "john@gmail.com", "0977035432");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.addUser(userDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void updateUser() {
        UserDto userDto = new UserDto(1, "John", 25, "john@!gmail.com", "+0977035432");
        User user = new User(1, "John", 25, "john@!gmail.com", "+0977035432");

        Optional<User> optionalUser = Optional.of(user);

        when(userService.getUserById(1)).thenReturn(optionalUser);

        UserDto updateUserDto = new UserDto(1, "Mike", 30, "mike@gmail.com", "+0977135432");
        User updateUser = new User(1, "Mike", 30, "mike@gmail.com", "+0977135432");

        when(userService.updateUser(user, userDto)).thenReturn(updateUser);
        when(userMapper.toDto(updateUser)).thenReturn(updateUserDto);

        UserDto result = userController.updateUser(1, userDto);

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(1)).updateUser(user, userDto);
        verify(userMapper, times(1)).toDto(updateUser);

        assertThat(result).isEqualTo(updateUserDto);
    }

    @Test
    public void updateUserNotFound() {
        Integer id = 1;
        UserDto userDto = new UserDto(1, "John", 25, "john@!gmail.com", "+0977035432");

        when(userService.getUserById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userController.updateUser(id, userDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    public void deleteUserByIdSuccess() {
        Integer userId = 1;

        User user = new User(userId, "John", 25, "john@gmail.com", "+0977035432");
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        String expectedResponse = "User with id " + userId + " was successfully deleted";
        String actualResponse = userController.delete(userId);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void deleteUserByIdNotFound() {
        Integer userId = -1;
        String expectedErrorMessage = "Error deleting user with id: null";
        assertEquals(expectedErrorMessage, userController.delete(userId));
    }
}