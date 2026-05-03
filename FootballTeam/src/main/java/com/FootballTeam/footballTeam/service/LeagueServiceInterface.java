package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.LeagueRequestDto;
import com.FootballTeam.footballTeam.dto.response.LeagueResponseDto;

import java.util.List;
import java.util.Optional;

public interface LeagueServiceInterface {

    LeagueResponseDto createLeague(LeagueRequestDto leagueRequestDto);

    List<LeagueResponseDto> getAllLeagues();

    Optional<LeagueResponseDto> getLeagueById(Long id);

    LeagueResponseDto updateLeague(Long id, LeagueRequestDto leagueRequestDto);

    void deleteLeague(Long id);

}