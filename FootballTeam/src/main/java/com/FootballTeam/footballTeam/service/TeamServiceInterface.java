package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.TeamRequestDto;
import com.FootballTeam.footballTeam.dto.response.TeamResponseDto;

import java.util.List;
import java.util.Optional;

public interface TeamServiceInterface {
    TeamResponseDto createTeam(TeamRequestDto teamRequestDto);
    Optional<TeamResponseDto> getTeamById(Long id);
    List<TeamResponseDto> getAllTeams();
    TeamResponseDto updateTeam(Long id, TeamRequestDto teamRequestDto);
    void deleteTeam(Long id);
    List<TeamResponseDto> getTeamsByFoundingYear(int year);
    List<TeamResponseDto> getTeamsByCoach(String coach);
    Optional<TeamResponseDto> getTeamByName(String teamName);

    // Metodi per le relazioni tra entità
    TeamResponseDto addPlayerToTeam(Long teamId, Long playerId);
    TeamResponseDto removePlayerFromTeam(Long teamId, Long playerId);
    TeamResponseDto addLeagueToTeam(Long teamId, Long leagueId);
    TeamResponseDto removeLeagueFromTeam(Long teamId, Long leagueId);
}