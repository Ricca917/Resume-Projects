package com.FootballTeam.footballTeam.controller;

import com.FootballTeam.footballTeam.dto.request.TeamRequestDto;
import com.FootballTeam.footballTeam.dto.response.TeamResponseDto;
import com.FootballTeam.footballTeam.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // Endpoint per visualizzare tutte le Squadre
    @GetMapping
    public ResponseEntity<List<TeamResponseDto>> getAllTeams() {
        List<TeamResponseDto> teams = teamService.getAllTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    // Endpoint per la creazione di un nuovo Team (metodo POST)
    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(@Valid @RequestBody TeamRequestDto teamDto) {
        TeamResponseDto savedTeamDto = teamService.createTeam(teamDto);
        return new ResponseEntity<>(savedTeamDto, HttpStatus.CREATED);
    }

    // Endpoint per trovare Squadre da Id (metodo GET)
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDto> getTeamById(@PathVariable Long id) {
        TeamResponseDto teamDto = teamService.getTeamById(id)
                .orElseThrow(() -> new NoSuchElementException("Squadra non trovata con ID: " + id));
        return new ResponseEntity<>(teamDto, HttpStatus.OK);
    }

    // Endpoint per aggiornare un team esistente (metodo PUT)
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponseDto> updateTeam(@PathVariable Long id, @Valid @RequestBody TeamRequestDto teamDto) {
        TeamResponseDto updatedTeamDto = teamService.updateTeam(id, teamDto);
        return new ResponseEntity<>(updatedTeamDto, HttpStatus.OK);
    }

    // Endpoint per l'eliminazione di un Team (metodo DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint per ottenere un Team dal nome (metodo GET)
    @GetMapping("/byName")
    public ResponseEntity<TeamResponseDto> getTeamByName(@RequestParam String name) {
        TeamResponseDto teamDto = teamService.getTeamByName(name)
                .orElseThrow(() -> new NoSuchElementException("Team not found with name: " + name));
        return new ResponseEntity<>(teamDto, HttpStatus.OK);
    }

    // Endpoint per aggiungere un giocatore a una Squadra (metodo POST)
    @PostMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<TeamResponseDto> addPlayerToTeam(@PathVariable Long teamId, @PathVariable Long playerId) {
        TeamResponseDto updatedTeamDto = teamService.addPlayerToTeam(teamId, playerId);
        return new ResponseEntity<>(updatedTeamDto, HttpStatus.OK);
    }

    // Endpoint per rimuovere un giocatore da un team esistente (metodo DELETE)
    @DeleteMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<TeamResponseDto> removePlayerFromTeam(@PathVariable Long teamId, @PathVariable Long playerId) {
        TeamResponseDto updatedTeamDto = teamService.removePlayerFromTeam(teamId, playerId);
        return new ResponseEntity<>(updatedTeamDto, HttpStatus.OK);
    }

    // Endpoint per aggiungere una Lega a una Squadra (metodo POST)
    @PostMapping("/{teamId}/leagues/{leagueId}")
    public ResponseEntity<TeamResponseDto> addLeagueToTeam(@PathVariable Long teamId, @PathVariable Long leagueId) {
        TeamResponseDto updatedTeamDto = teamService.addLeagueToTeam(teamId, leagueId);
        return new ResponseEntity<>(updatedTeamDto, HttpStatus.OK);
    }

    // Endpoint per rimuovere una Lega da una Squadra (metodo DELETE)
    @DeleteMapping("/{teamId}/leagues/{leagueId}")
    public ResponseEntity<TeamResponseDto> removeLeagueFromTeam(@PathVariable Long teamId, @PathVariable Long leagueId) {
        TeamResponseDto updatedTeamDto = teamService.removeLeagueFromTeam(teamId, leagueId);
        return new ResponseEntity<>(updatedTeamDto, HttpStatus.OK);
    }

    // Endpoint per cercare squadre tramite anno di fondazione
    @GetMapping("/byFoundingYear")
    public ResponseEntity<List<TeamResponseDto>> getTeamsByFoundingYear(@RequestParam int year) {
        List<TeamResponseDto> teams = teamService.getTeamsByFoundingYear(year);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    // Endpoint per cercare squadre tramite il coach/allenatore
    @GetMapping("/byCoach")
    public ResponseEntity<List<TeamResponseDto>> getTeamsByCoach(@RequestParam String coach) {
        List<TeamResponseDto> teams = teamService.getTeamsByCoach(coach);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
}