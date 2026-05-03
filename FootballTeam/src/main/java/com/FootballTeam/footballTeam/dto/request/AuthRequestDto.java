package com.FootballTeam.footballTeam.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequestDto {

    @NotBlank(message = "L'username non può essere vuoto")
    @Size(min = 3, max = 50, message = "L'username deve essere compreso fra 3 e 50 caratteri")
    private String username;

    @NotBlank(message = "La password non può essere vuota")
    @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
    private String password;

    private String role;

    // Costruttore vuoto
    public AuthRequestDto() {
    }

    // Costruttore con attributi
    public AuthRequestDto(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter e Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}