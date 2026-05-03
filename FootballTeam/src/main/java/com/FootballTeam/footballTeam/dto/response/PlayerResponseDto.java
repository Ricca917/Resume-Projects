package com.FootballTeam.footballTeam.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public class PlayerResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String nationality;
    private LocalDate dateOfBirth;
    private String position;
    private int jerseyNumber;
    private double salary;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long contractId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long teamId;

    private int age;
    private int appearances;
    private int goals;
    private Boolean isFreeAgent;
    private String teamName;


    // Costruttore vuoto
    public PlayerResponseDto() {
    }

    // Costruttore con gli attributi
    public PlayerResponseDto(Long id, String firstName, String lastName,String nationality, LocalDate dateOfBirth, String position,int jerseyNumber, double salary, Long contractId, Long teamId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.salary = salary;
        this.contractId = contractId;
        this.teamId = teamId;

    }

    // Getter e Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    // Getter e Setter
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public Boolean getFreeAgent() {
        return isFreeAgent;
    }

    public void setFreeAgent(Boolean freeAgent) {
        isFreeAgent = freeAgent;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}