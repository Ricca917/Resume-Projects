package com.FootballTeam.footballTeam.dto.response;

public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";

    // Costruttore vuoto
    public AuthResponseDto() {
    }

    // Costruttore con attributi
    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    // Getter
    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }


}