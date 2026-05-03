package com.FootballTeam.footballTeam.controller;

import com.FootballTeam.footballTeam.dto.request.AuthRequestDto;
import com.FootballTeam.footballTeam.dto.response.AuthResponseDto;
import com.FootballTeam.footballTeam.model.User;
import com.FootballTeam.footballTeam.model.Role;
import com.FootballTeam.footballTeam.repository.UserRepository;
import com.FootballTeam.footballTeam.security.JwtProvider;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticateUser(@Valid @RequestBody AuthRequestDto authRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getUsername(),
                        authRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AuthRequestDto authRequestDto) {
        try {
            if (userRepository.existsByUsername(authRequestDto.getUsername())) {
                return new ResponseEntity<>("Username è già in uso!", HttpStatus.BAD_REQUEST);
            }

            if (authRequestDto.getRole() == null || authRequestDto.getRole().trim().isEmpty()) {
                return new ResponseEntity<>("Il ruolo è obbligatorio per la registrazione!", HttpStatus.BAD_REQUEST);
            }

            User user = new User();
            user.setUsername(authRequestDto.getUsername());
            user.setPassword(passwordEncoder.encode(authRequestDto.getPassword()));

            try {
                Role userRole = Role.valueOf(authRequestDto.getRole().toUpperCase());
                user.setRole(userRole);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>("Ruolo non valido: " + authRequestDto.getRole() + ". Ruoli consentiti: USER, ADMIN.", HttpStatus.BAD_REQUEST);
            }

            userRepository.save(user);

            return new ResponseEntity<>("Utente registrato con successo!", HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.error("Errore durante la registrazione utente", ex);
            return new ResponseEntity<>("Si è verificato un errore interno del server!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
