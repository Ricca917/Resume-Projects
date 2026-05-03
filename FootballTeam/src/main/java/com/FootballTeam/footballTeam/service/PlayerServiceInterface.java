package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.PlayerRequestDto;
import com.FootballTeam.footballTeam.dto.response.PlayerResponseDto;

import java.util.List;
import java.util.Optional;

public interface PlayerServiceInterface {
    PlayerResponseDto createPlayer(PlayerRequestDto playerRequestDto);
    Optional<PlayerResponseDto> getPlayerById(Long id);
    List<PlayerResponseDto> getAllPlayers();
    PlayerResponseDto updatePlayer(Long id, PlayerRequestDto playerRequestDto);
    void deletePlayer(Long id);
    List<PlayerResponseDto> getPlayersByTeamId(Long teamId);
    List<PlayerResponseDto> getPlayersByPosition(String position);
    List<PlayerResponseDto> getPlayersByNationality(String nationality);
    List<PlayerResponseDto> getFreePlayers();

}
