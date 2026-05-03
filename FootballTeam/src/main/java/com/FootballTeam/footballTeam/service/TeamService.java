package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.TeamRequestDto;
import com.FootballTeam.footballTeam.dto.response.LeagueResponseDto;
import com.FootballTeam.footballTeam.dto.response.PlayerResponseDto;
import com.FootballTeam.footballTeam.dto.response.TeamResponseDto;
import com.FootballTeam.footballTeam.model.League;
import com.FootballTeam.footballTeam.model.Player;
import com.FootballTeam.footballTeam.model.Team;
import com.FootballTeam.footballTeam.repository.LeagueRepository;
import com.FootballTeam.footballTeam.repository.PlayerRepository;
import com.FootballTeam.footballTeam.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamService implements TeamServiceInterface {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final LeagueRepository leagueRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository, LeagueRepository leagueRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.leagueRepository = leagueRepository;
    }

    // Metodo per convertire Player in PlayerResponseDto
    private PlayerResponseDto convertPlayerToDto(Player player) {
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
        if (player.getTeam() != null) {
            dto.setTeamId(player.getTeam().getId());
            dto.setTeamName(player.getTeam().getTeamName());
        }
        return dto;
    }

    // Metodo per convertire League in LeagueResponseDto
    private LeagueResponseDto convertLeagueToDto(League league) {
        return new LeagueResponseDto(league.getId(), league.getName());
    }

    // Metodo Mapping TeamRequestDto in Team
    private Team convertToEntity(TeamRequestDto teamDto) {
        Team team = new Team();
        team.setTeamName(teamDto.getTeamName());
        team.setFoundingYear(teamDto.getFoundingYear());
        team.setPresident(teamDto.getPresident());
        team.setCoach(teamDto.getCoach());

        // Gestione delle leghe per la creazione/conversione
        if (teamDto.getLeagueIds() != null && !teamDto.getLeagueIds().isEmpty()) {
            List<League> leagues = leagueRepository.findAllById(teamDto.getLeagueIds());
            if (leagues.size() != teamDto.getLeagueIds().size()) {
                Set<Long> foundIds = leagues.stream().map(League::getId).collect(Collectors.toSet());
                String missingIds = teamDto.getLeagueIds().stream()
                        .filter(id -> !foundIds.contains(id))
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                throw new NoSuchElementException("Uno o più Campionati/Coppe con ID: [" + missingIds + "] non trovati.");
            }

            for (League league : leagues) {
                team.addLeague(league);
            }
        }
        return team;
    }

    // Metodo Mapping Team in TeamResponseDto
    private TeamResponseDto convertToDto(Team team) {
        TeamResponseDto dto = new TeamResponseDto();
        dto.setId(team.getId());
        dto.setTeamName(team.getTeamName());
        dto.setFoundingYear(team.getFoundingYear());
        dto.setPresident(team.getPresident());
        dto.setCoach(team.getCoach());

        // Mappa la lista dei giocatori
        if (team.getRosterPlayers() != null && !team.getRosterPlayers().isEmpty()) {
            dto.setPlayers(team.getRosterPlayers().stream()
                    .map(this::convertPlayerToDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setPlayers(new ArrayList<>());
        }

        // Mappa la lista delle leghe
        if (team.getLeagues() != null && !team.getLeagues().isEmpty()) {
            dto.setLeagues(team.getLeagues().stream()
                    .map(this::convertLeagueToDto)
                    .collect(Collectors.toSet()));
        } else {
            dto.setLeagues(new HashSet<>());
        }
        return dto;
    }



    @Override
    @Transactional
    public TeamResponseDto createTeam(TeamRequestDto teamRequestDto) {
        Team team = convertToEntity(teamRequestDto);
        team = teamRepository.save(team);
        return convertToDto(team);
    }

    @Override
    public Optional<TeamResponseDto> getTeamById(Long id) {
        return teamRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public List<TeamResponseDto> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TeamResponseDto updateTeam(Long id, TeamRequestDto teamRequestDto) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Squadra non trovata con ID: " + id));

        // Aggiorna i campi base
        existingTeam.setTeamName(teamRequestDto.getTeamName());
        existingTeam.setFoundingYear(teamRequestDto.getFoundingYear());
        existingTeam.setPresident(teamRequestDto.getPresident());
        existingTeam.setCoach(teamRequestDto.getCoach());

        // Gestione delle leghe:
        Set<League> newLeagues = new HashSet<>();
        if (teamRequestDto.getLeagueIds() != null && !teamRequestDto.getLeagueIds().isEmpty()) {
            List<League> fetchedLeagues = leagueRepository.findAllById(teamRequestDto.getLeagueIds());
            if (fetchedLeagues.size() != teamRequestDto.getLeagueIds().size()) {
                Set<Long> foundIds = fetchedLeagues.stream().map(League::getId).collect(Collectors.toSet());
                String missingIds = teamRequestDto.getLeagueIds().stream()
                        .filter(leagueId -> !foundIds.contains(leagueId))
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                throw new NoSuchElementException("Una o più Campionati/Coppe con ID: [" + missingIds + "] non trovate per l'aggiornamento.");
            }
            newLeagues.addAll(fetchedLeagues);
        }

        Set<League> leaguesToRemove = new HashSet<>(existingTeam.getLeagues());
        leaguesToRemove.removeAll(newLeagues);
        for (League league : leaguesToRemove) {
            existingTeam.removeLeague(league);
        }

        Set<League> leaguesToAdd = new HashSet<>(newLeagues);
        leaguesToAdd.removeAll(existingTeam.getLeagues());
        for (League league : leaguesToAdd) {
            existingTeam.addLeague(league);
        }

        Team updatedTeam = teamRepository.save(existingTeam);
        return convertToDto(updatedTeam);
    }

    @Override
    @Transactional
    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Squadra non trovata con ID: " + id));


        if (team.getRosterPlayers() != null) {
            new ArrayList<>(team.getRosterPlayers()).forEach(player -> {
                player.setTeam(null);
                player.setIsFreeAgent(true);
                playerRepository.save(player);
            });
            team.getRosterPlayers().clear();
        }


        if (team.getLeagues() != null) {
            new HashSet<>(team.getLeagues()).forEach(team::removeLeague);
        }

        teamRepository.deleteById(id);
    }

    @Override
    public List<TeamResponseDto> getTeamsByFoundingYear(int year) {
        return teamRepository.findByFoundingYear(year).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamResponseDto> getTeamsByCoach(String coach) {
        return teamRepository.findByCoach(coach).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TeamResponseDto> getTeamByName(String teamName) {
        return teamRepository.findByTeamName(teamName)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public TeamResponseDto addPlayerToTeam(Long teamId, Long playerId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Squadra non trovata con ID: " + teamId));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Giocatore non trovato con ID: " + playerId));

        if (player.getTeam() != null && player.getTeam().getId().equals(teamId)) {
            return convertToDto(team);
        }

        if (player.getTeam() != null && !player.getTeam().getId().equals(teamId)) {
            throw new IllegalArgumentException("Giocatore con ID " + playerId + " è già associato ad una squadra con ID: " + player.getTeam().getId());
        }

        team.addPlayer(player);
        player.setIsFreeAgent(false);
        playerRepository.save(player);
        Team updatedTeam = teamRepository.save(team);
        return convertToDto(updatedTeam);
    }

    @Override
    @Transactional
    public TeamResponseDto removePlayerFromTeam(Long teamId, Long playerId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Squadra non trovata con ID: " + teamId));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Giocatore non trovato con ID: " + playerId));


        if (player.getTeam() == null || !player.getTeam().getId().equals(teamId)) {
            throw new IllegalArgumentException("Giocatore con ID" + playerId + " non è associato ad una squadra con ID:" + teamId + ".");
        }

        team.removePlayer(player);
        player.setIsFreeAgent(true);
        playerRepository.save(player);
        Team updatedTeam = teamRepository.save(team);
        return convertToDto(updatedTeam);
    }

    @Override
    @Transactional
    public TeamResponseDto addLeagueToTeam(Long teamId, Long leagueId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Squadra non trovata con ID: " + teamId));
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new NoSuchElementException("Campionato/Coppa non trovata con ID: " + leagueId));

        if (team.getLeagues().contains(league)) {
            return convertToDto(team);
        }

        team.addLeague(league);
        Team updatedTeam = teamRepository.save(team);
        return convertToDto(updatedTeam);
    }

    @Override
    @Transactional
    public TeamResponseDto removeLeagueFromTeam(Long teamId, Long leagueId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Squadra non trovata con ID: " + teamId));
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new NoSuchElementException("Campionato/Coppa non trovata con ID: " + leagueId));

        if (!team.getLeagues().contains(league)) {
            throw new IllegalArgumentException("Squadra con ID: " + teamId + " non è parte di quel Campionato/Coppa " + leagueId + ".");
        }

        team.removeLeague(league);
        Team updatedTeam = teamRepository.save(team);
        return convertToDto(updatedTeam);
    }
}