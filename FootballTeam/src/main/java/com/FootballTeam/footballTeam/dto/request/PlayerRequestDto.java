package com.FootballTeam.footballTeam.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PlayerRequestDto {

    @NotBlank(message = "Il nome non può essere vuoto")
    private String firstName;

    @NotBlank(message = "Il cognome non può essere vuoto")
    private String lastName;

    @NotBlank(message = "La nazionalità non può essere vuota")
    private String nationality;

    @NotNull(message = "La data di nascita non può essere nulla")
    private LocalDate dateOfBirth;

    @NotNull(message = "Specificare se il giocatore è libero")
    private Boolean isFreeAgent;

    @Min(value = 0, message = "L'età non può essere negativa")
    private int age;

    @NotBlank(message = "La posizione non può essere vuota")
    private String position;

    @Min(value = 1, message = "Il numero di maglia deve essere almeno 1")
    private int jerseyNumber;

    @Min(value = 0, message = "Le presenze non possono essere negative")
    private int appearances;

    @Min(value = 0, message = "I goal non possono essere negativi")
    private int goals;

    private Long teamId;

    // Costruttore vuoto
    public PlayerRequestDto() {
    }

    // Costruttore con attributi
    public PlayerRequestDto(String firstName, String lastName, String nationality, int age, LocalDate dateOfBirth, String position, int jerseyNumber, int appearances, int goals, Boolean isFreeAgent, Long teamId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.appearances = appearances;
        this.goals = goals;
        this.isFreeAgent = isFreeAgent;
        this.teamId = teamId;
    }

    // Getter e Setter

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getIsFreeAgent() {
        return isFreeAgent;
    }

    public void setIsFreeAgent(Boolean freeAgent) {
        isFreeAgent = freeAgent;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public int getAppearances() {
        return appearances;
    }

    public void setAppearances(int appearances) {
        this.appearances = appearances;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}