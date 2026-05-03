package com.FootballTeam.footballTeam.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Entity
@Table(name = "teams")

public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;
    private int foundingYear;

    @Column(nullable = false)
    private String president;

    @Column(nullable = false)
    private String coach;

    @OneToMany(mappedBy = "team",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<Player> rosterPlayers = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "team_leagues", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "league_id"))
    private Set<League> leagues = new HashSet<>();

    // Costruttore vuoto
    public Team() {}

    //Costruttore base
    public Team(String teamName, int foundingYear, String president, String coach) {
        this.teamName = teamName;
        this.foundingYear = foundingYear;
        this.president = president;
        this.coach = coach;

    }

    // Getter e Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getFoundingYear() {
        return foundingYear;
    }

    public void setFoundingYear(int foundingYear) {
        this.foundingYear = foundingYear;
    }

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public List<Player> getRosterPlayers() {
        return rosterPlayers;
    }

    public Set<League> getLeagues() {
        return leagues;
    }

    public void setRosterPlayers(List<Player> newRosterPlayers) {
        if (this.rosterPlayers != null) {
            for (Player player : new ArrayList<>(this.rosterPlayers)) {
                removePlayer(player);
            }
        }
        if (newRosterPlayers != null) {
            for (Player player : newRosterPlayers) {
                addPlayer(player);
            }
        }
    }

    // Metodo per aggiungere un giocatore alla squadra
    public void addPlayer(Player player) {
        if (player != null && !this.rosterPlayers.contains(player)) {
            this.rosterPlayers.add(player);
            player.setTeam(this);
        }
    }

    // Metodo per la rimozione di un giocatore dalla squadra
    public void removePlayer(Player player) {
        if (player != null && this.rosterPlayers.remove(player)) {
            player.setTeam(null);
        }
    }

    // Metodo per Aggiungere una Lega/Campionato
    public void addLeague(League league) {
        if (league != null && !this.leagues.contains(league)) {
            this.leagues.add(league);
            league.getTeams().add(this);
        }
    }

    // Metodo per Rimuovere una Lega/Campionato
    public void removeLeague(League league) {
        if (league != null && this.leagues.remove(league)) {
            league.getTeams().remove(this);
        }
    }

    // Metodo per stampare info sulla squadra
    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", teamName='" + teamName + '\'' +
                ", foundingYear=" + foundingYear +
                ", president='" + president + '\'' +
                ", coach='" + coach + '\'' +
                '}';
    }
}