package com.FootballTeam.footballTeam.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "leagues")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "country", nullable = false)
    private String country;

    // Definisco la relazione Many to Many fra Lega e Squadre
    @ManyToMany(mappedBy = "leagues", fetch = FetchType.LAZY)
    private Set<Team> teams = new HashSet<>();

    // Costruttore vuoto
    public League() {
    }

    // Costruttore con Attributi
    public League(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public League(Long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    // Getter e Setter

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // Metodo per Aggiungere Squadre alla Lega/Campionato
    public void addTeam(Team team) {
        this.teams.add(team);
        team.getLeagues().add(this);
    }

    // Metodo per Togliere Squadre alla Lega/Campionato
    public void removeTeam(Team team) {
        this.teams.remove(team);
        team.getLeagues().remove(this);
    }

    // Metodo ToString per le informazioni relative alla Lega/Campionato
    @Override
    public String toString() {
        return "League{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return Objects.equals(id, league.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}