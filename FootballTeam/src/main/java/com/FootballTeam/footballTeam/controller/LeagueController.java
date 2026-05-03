package com.FootballTeam.footballTeam.controller;

import com.FootballTeam.footballTeam.dto.request.LeagueRequestDto;
import com.FootballTeam.footballTeam.dto.response.LeagueResponseDto;
import com.FootballTeam.footballTeam.service.LeagueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private final LeagueService leagueService;

    @Autowired // Costruttore per l'iniezione delle dipendenze
    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    // Endpoint per creare una nuova lega
    // POST /api/leagues
    @PostMapping
    public ResponseEntity<LeagueResponseDto> createLeague(@Valid @RequestBody LeagueRequestDto leagueRequestDto) {
        LeagueResponseDto createdLeague = leagueService.createLeague(leagueRequestDto);
        return new ResponseEntity<>(createdLeague, HttpStatus.CREATED);
    }

    // Endpoint per ottenere tutte le leghe
    // GET /api/leagues
    @GetMapping
    public ResponseEntity<List<LeagueResponseDto>> getAllLeagues() {
        List<LeagueResponseDto> leagues = leagueService.getAllLeagues();
        return new ResponseEntity<>(leagues, HttpStatus.OK);
    }

    // Endpoint per ottenere una lega tramite ID
    // GET /api/leagues/{id}
    @GetMapping("/{id}")
    public ResponseEntity<LeagueResponseDto> getLeagueById(@PathVariable Long id) {
        LeagueResponseDto leagueDto = leagueService.getLeagueById(id)
                .orElseThrow(() -> new NoSuchElementException("Campionato/Coppa non trovata con ID: " + id));
        return new ResponseEntity<>(leagueDto, HttpStatus.OK);
    }

    // Endpoint per aggiornare una lega esistente
    // PUT /api/leagues/{id}
    @PutMapping("/{id}")
    public ResponseEntity<LeagueResponseDto> updateLeague(@PathVariable Long id, @Valid @RequestBody LeagueRequestDto leagueRequestDto) {
        LeagueResponseDto updatedLeague = leagueService.updateLeague(id, leagueRequestDto);
        return new ResponseEntity<>(updatedLeague, HttpStatus.OK);
    }

    // Endpoint per eliminare una lega
    // DELETE /api/leagues/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeague(@PathVariable Long id) {
        leagueService.deleteLeague(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}