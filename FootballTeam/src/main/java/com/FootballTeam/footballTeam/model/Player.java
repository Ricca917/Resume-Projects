package com.FootballTeam.footballTeam.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String nationality;

    @Column(name = "jersey_number")
    private int jerseyNumber;

    private int appearances;
    private int goals;

    @Column(nullable = false)
    private double salary;

    @Column(nullable = false)
    private boolean isFreeAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Contract contract;

    // Costruttore vuoto per la JPA
    public Player() {
    }

    // Costruttore con Attributi
    public Player(String firstName, String lastName, LocalDate dateOfBirth, int age, String position,
                  String nationality, int jerseyNumber, int appearances, int goals,
                  boolean isFreeAgent, Team team, Contract contract) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.position = position;
        this.nationality = nationality;
        this.jerseyNumber = jerseyNumber;
        this.appearances = appearances;
        this.goals = goals;
        this.isFreeAgent = isFreeAgent;
        this.team = team;
        this.contract = contract;
        this.salary = 0.0;
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
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean getIsFreeAgent() {
        return isFreeAgent;
    }

    public void setIsFreeAgent(boolean isFreeAgent) {
        this.isFreeAgent = isFreeAgent;
    }

    public boolean isFreeAgent() {
        return isFreeAgent;
    }

    public void setFreeAgent(boolean freeAgent) {
        isFreeAgent = freeAgent;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
        if (contract != null && contract.getPlayer() != this) {
            contract.setPlayer(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                ", position='" + position + '\'' +
                ", nationality='" + nationality + '\'' +
                ", jerseyNumber=" + jerseyNumber +
                ", appearances=" + appearances +
                ", goals=" + goals +
                ", salary=" + salary +
                ", isFreeAgent=" + isFreeAgent +
                ", teamId=" + (team != null ? team.getId() : "null") +
                '}';
    }
}