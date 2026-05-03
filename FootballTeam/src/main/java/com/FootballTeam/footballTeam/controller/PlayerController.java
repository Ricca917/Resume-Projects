package com.FootballTeam.footballTeam.controller;

import com.FootballTeam.footballTeam.dto.request.PlayerRequestDto;
import com.FootballTeam.footballTeam.dto.response.PlayerResponseDto;
import com.FootballTeam.footballTeam.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // Endpoint per creare un Giocatore
    @PostMapping
    public ResponseEntity<PlayerResponseDto> createPlayer(@Valid @RequestBody PlayerRequestDto playerDto) {
        PlayerResponseDto savedPlayerDto = playerService.createPlayer(playerDto);
        return new ResponseEntity<>(savedPlayerDto, HttpStatus.CREATED);
    }

    // Endpoint per ottenere TUTTI i giocatori (Metodo GET)
    @GetMapping
    public ResponseEntity<List<PlayerResponseDto>> getAllPlayers() {
        List<PlayerResponseDto> playerDtos = playerService.getAllPlayers();
        return new ResponseEntity<>(playerDtos, HttpStatus.OK);
    }

    // Endpoint per ottenere un giocatore tramite Id (metodo GET)
    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDto> getPlayerById(@PathVariable Long id) {
        PlayerResponseDto playerDto = playerService.getPlayerById(id)
                .orElseThrow(() -> new NoSuchElementException("Player non trovato con ID: " + id));
        return new ResponseEntity<>(playerDto, HttpStatus.OK);
    }

    // Endpoint per aggiornare un giocatore esistente (metodo PUT)
    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponseDto> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerRequestDto playerDto) {
        PlayerResponseDto updatedPlayerDto = playerService.updatePlayer(id, playerDto);
        return new ResponseEntity<>(updatedPlayerDto, HttpStatus.OK);
    }

    // Endpoint per eliminare un giocatore (metodo DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint per ottenere giocatori per ruolo (metodo GET)
    @GetMapping("/byPosition")
    public ResponseEntity<List<PlayerResponseDto>> getPlayersByPosition(@RequestParam String position) {
        List<PlayerResponseDto> playerDtos = playerService.getPlayersByPosition(position);
        return new ResponseEntity<>(playerDtos, HttpStatus.OK);
    }

    // Endpoint per ottenere giocatori svincolati (metodo GET)
    @GetMapping("/free")
    public ResponseEntity<List<PlayerResponseDto>> getFreePlayers() {
        List<PlayerResponseDto> playerDtos = playerService.getFreePlayers();
        return new ResponseEntity<>(playerDtos, HttpStatus.OK);
    }

    // Endpoint per ottenere giocatori per nazionalità (metodo GET)
    @GetMapping("/byNationality")
    public ResponseEntity<List<PlayerResponseDto>> getPlayersByNationality(@RequestParam String nationality) {
        List<PlayerResponseDto> playerDtos = playerService.getPlayersByNationality(nationality);
        return new ResponseEntity<>(playerDtos, HttpStatus.OK);
    }
}