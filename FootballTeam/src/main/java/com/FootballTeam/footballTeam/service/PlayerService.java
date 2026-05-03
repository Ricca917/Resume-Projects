package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.PlayerRequestDto;
import com.FootballTeam.footballTeam.dto.response.PlayerResponseDto;
import com.FootballTeam.footballTeam.model.Player;
import com.FootballTeam.footballTeam.model.Team;
import com.FootballTeam.footballTeam.repository.PlayerRepository;
import com.FootballTeam.footballTeam.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService implements PlayerServiceInterface {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    // Metodo Mapping PlayerRequestDto in Player
    private Player convertToEntity(PlayerRequestDto playerDto) {
        Player player = new Player();
        player.setFirstName(playerDto.getFirstName());
        player.setLastName(playerDto.getLastName());
        player.setNationality(playerDto.getNationality());
        player.setDateOfBirth(playerDto.getDateOfBirth());

        // Check dell'età del player
        if (playerDto.getAge() <= 0 && playerDto.getDateOfBirth() != null) {
            player.setAge(Period.between(playerDto.getDateOfBirth(), LocalDate.now()).getYears());
        } else {
            player.setAge(playerDto.getAge());
        }
        player.setPosition(playerDto.getPosition());
        player.setJerseyNumber(playerDto.getJerseyNumber());
        player.setAppearances(playerDto.getAppearances());
        player.setGoals(playerDto.getGoals());
        player.setIsFreeAgent(playerDto.getIsFreeAgent());

        // Relazione Player - Team
        if (playerDto.getTeamId() != null) {
            Team team = teamRepository.findById(playerDto.getTeamId())
                    .orElseThrow(() -> new NoSuchElementException("Team with ID " + playerDto.getTeamId() + " not found."));
            player.setTeam(team);
            player.setIsFreeAgent(false);
        } else {
            player.setTeam(null);
            player.setIsFreeAgent(true);
        }
        return player;
    }

    // Metodo Mapping Player in PlayerResponseDto
    private PlayerResponseDto convertToDto(Player player) {
        PlayerResponseDto dto = new PlayerResponseDto();
        dto.setId(player.getId());
        dto.setFirstName(player.getFirstName());
        dto.setLastName(player.getLastName());
        dto.setNationality(player.getNationality());
        dto.setAge(player.getAge());
        dto.setDateOfBirth(player.getDateOfBirth());
        dto.setPosition(player.getPosition());
        dto.setJerseyNumber(player.getJerseyNumber());
        dto.setAppearances(player.getAppearances());
        dto.setGoals(player.getGoals());
        dto.setFreeAgent(player.getIsFreeAgent());

        // Se il giocatore è associato a una squadra, includi nome e ID
        if (player.getTeam() != null) {
            dto.setTeamId(player.getTeam().getId());
            dto.setTeamName(player.getTeam().getTeamName());
        }
        return dto;
    }

    @Override
    @Transactional
    public PlayerResponseDto createPlayer(PlayerRequestDto playerRequestDto) {
        Player player = convertToEntity(playerRequestDto);
        player = playerRepository.save(player);

        if (player.getTeam() != null) {
            player.getTeam().addPlayer(player);
            teamRepository.save(player.getTeam());
        }
        return convertToDto(player);
    }

    @Override
    public Optional<PlayerResponseDto> getPlayerById(Long id) {
        return playerRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public List<PlayerResponseDto> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlayerResponseDto updatePlayer(Long id, PlayerRequestDto playerRequestDto) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Giocatore non trovato con ID:" + id));

        Team oldTeam = existingPlayer.getTeam();
        if (oldTeam != null) {
            oldTeam.removePlayer(existingPlayer);
        }

        existingPlayer.setFirstName(playerRequestDto.getFirstName());
        existingPlayer.setLastName(playerRequestDto.getLastName());
        existingPlayer.setNationality(playerRequestDto.getNationality());
        existingPlayer.setDateOfBirth(playerRequestDto.getDateOfBirth());
        if (playerRequestDto.getAge() <= 0 && playerRequestDto.getDateOfBirth() != null) {
            existingPlayer.setAge(Period.between(playerRequestDto.getDateOfBirth(), LocalDate.now()).getYears());
        } else {
            existingPlayer.setAge(playerRequestDto.getAge());
        }
        existingPlayer.setPosition(playerRequestDto.getPosition());
        existingPlayer.setJerseyNumber(playerRequestDto.getJerseyNumber());
        existingPlayer.setAppearances(playerRequestDto.getAppearances());
        existingPlayer.setGoals(playerRequestDto.getGoals());
        existingPlayer.setIsFreeAgent(playerRequestDto.getIsFreeAgent());

        // Associazione con la nuova squadra o FreeAgency
        if (playerRequestDto.getTeamId() != null) {
            Team newTeam = teamRepository.findById(playerRequestDto.getTeamId())
                    .orElseThrow(() -> new NoSuchElementException("Squadra con ID" + playerRequestDto.getTeamId() + " non trovata."));
            existingPlayer.setTeam(newTeam);
            newTeam.addPlayer(existingPlayer);
            existingPlayer.setIsFreeAgent(false);
            teamRepository.save(newTeam);
        } else {
            existingPlayer.setTeam(null);
            existingPlayer.setIsFreeAgent(true);
        }
        Player updatedPlayer = playerRepository.save(existingPlayer);

        if (oldTeam != null && (existingPlayer.getTeam() == null || !oldTeam.getId().equals(existingPlayer.getTeam().getId()))) {
            teamRepository.save(oldTeam);
        }
        return convertToDto(updatedPlayer);

    }

    @Override
    @Transactional
    public void deletePlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Giocatore non trovato con ID" + id));
        if (player.getTeam() != null) {
            player.getTeam().removePlayer(player);
            teamRepository.save(player.getTeam());
        }
        playerRepository.deleteById(id);
    }

    @Override
    public List<PlayerResponseDto> getPlayersByTeamId(Long teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Squadra con ID " + teamId + " non trovata."));
        return playerRepository.findByTeamId(teamId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerResponseDto> getPlayersByPosition(String position) {
        return playerRepository.findByPosition(position).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerResponseDto> getPlayersByNationality(String nationality) {
        return playerRepository.findByNationality(nationality).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerResponseDto> getFreePlayers() {
        return playerRepository.findByIsFreeAgentTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
