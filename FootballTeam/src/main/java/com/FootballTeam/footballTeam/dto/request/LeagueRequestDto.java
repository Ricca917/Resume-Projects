package com.FootballTeam.footballTeam.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LeagueRequestDto {

    @NotBlank(message = "Il nome della lega non può essere vuoto")
    private String name;

    @NotBlank(message = "Il paese della lega non può essere vuoto")
    private String country;

    // Costruttore vuoto
    public LeagueRequestDto() {
    }

    // Costruttore con attributi
    public LeagueRequestDto(String name, String country) {
        this.name = name;
        this.country = country;
    }

    // Getter e Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}