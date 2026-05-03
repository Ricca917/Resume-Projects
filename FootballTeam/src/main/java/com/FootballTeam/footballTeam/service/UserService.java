package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.UserRequestDto;
import com.FootballTeam.footballTeam.dto.response.UserResponseDto;
import com.FootballTeam.footballTeam.model.Role;
import com.FootballTeam.footballTeam.model.User;
import com.FootballTeam.footballTeam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo helper per convertire DTO in Entità
    private User convertToEntity(UserRequestDto userRequestDto) {
        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        Role assignedRole = Role.USER;
        if (userRequestDto.getRole() != null) {
            try {
                assignedRole = Role.valueOf(userRequestDto.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Errore di conversione ruolo in convertToEntity: " + userRequestDto.getRole() + ". Assegnato ruolo di default USER.");

            }
        }
        user.setRole(assignedRole);
        return user;
    }

    // Metodo helper per convertire Entità in DTO
    private UserResponseDto convertToDto(User user) {
        String roleName = "Sconosciuto";
        if (user.getRole() != null) {
            try {
                roleName = user.getRole().name();
            } catch (Exception e) {

                System.err.println("Errore durante l'ottenimento del nome del ruolo per l'utente " + user.getUsername() + ": " + e.getMessage());
            }
        } else {
            System.err.println("Attenzione: Ruolo nullo per l'utente: " + user.getUsername() + ". Assegnato 'UNKNOWN'.");
        }
        return new UserResponseDto(user.getId(), user.getUsername(), roleName);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + userRequestDto.getUsername() + "' already exists.");
        }
        User user = convertToEntity(userRequestDto);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }


    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public Optional<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utente non trovato con ID: " + id));

        if (!existingUser.getUsername().equals(userRequestDto.getUsername()) &&
                userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username '" + userRequestDto.getUsername() + "' esiste già ");
        }

        existingUser.setUsername(userRequestDto.getUsername());
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }

        Role assignedRole = Role.USER;
        if (userRequestDto.getRole() != null) {
            try {
                assignedRole = Role.valueOf(userRequestDto.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Ruolo non valido  " + userRequestDto.getRole() + ". ruolo attuale assegnato.");
                assignedRole = existingUser.getRole();
            }
        }
        existingUser.setRole(assignedRole);


        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Utente non trovato con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}