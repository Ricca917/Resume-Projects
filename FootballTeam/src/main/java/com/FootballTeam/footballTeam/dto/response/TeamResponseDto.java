package com.FootballTeam.footballTeam.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TeamResponseDto {
    private Long id;
    private String teamName;
    private Integer foundingYear;
    private String president;
    private String coach;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PlayerResponseDto> players;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<LeagueResponseDto>  leagues;


    // Costruttore vuoto
    public TeamResponseDto() {
        this.players = new java.util.ArrayList<>();
        this.leagues = new java.util.HashSet<>();
    }

    // Costruttore con attributi
    public TeamResponseDto(Long id, String teamName, Integer foundingYear, String president, String coach, List<PlayerResponseDto> players, Set<LeagueResponseDto> leagues) {
        this.id = id;
        this.teamName = teamName;
        this.foundingYear = foundingYear;
        this.president = president;
        this.coach = coach;
        this.players = players != null ? players : new java.util.ArrayList<>();
        this.leagues = leagues != null ? leagues : new java.util.HashSet<>();
    }

    // Getter e Setter

    public Long getId() {
        return id;
    }

    public String getTeamName() {
        return teamName;
    }

    public Integer getFoundingYear() {
        return foundingYear;
    }

    public String getPresident() {
        return president;
    }

    public String getCoach() {
        return coach;
    }

    public List<PlayerResponseDto> getPlayers() {
        return players;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setFoundingYear(Integer foundingYear) {
        this.foundingYear = foundingYear;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public void setPlayers(List<PlayerResponseDto> players) {
        this.players = players;
    }

    public Set<LeagueResponseDto> getLeagues() {
        return leagues;
    }

    public void setLeagues(Set<LeagueResponseDto> leagues) {
        this.leagues = leagues;
    }
}
