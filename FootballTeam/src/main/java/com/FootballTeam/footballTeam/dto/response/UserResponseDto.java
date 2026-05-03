package com.FootballTeam.footballTeam.dto.response;

public class UserResponseDto {
    private Long id;
    private String username;
    private String role;

    // Costruttore vuoto
    public UserResponseDto() {
    }

    // Costruttore con Attributi
    public UserResponseDto(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Getter e Setter

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }
}