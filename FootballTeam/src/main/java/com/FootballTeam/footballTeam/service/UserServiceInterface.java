package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.UserRequestDto;
import com.FootballTeam.footballTeam.dto.response.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    List<UserResponseDto> getAllUsers();
    Optional<UserResponseDto> getUserById(Long id);
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);

}