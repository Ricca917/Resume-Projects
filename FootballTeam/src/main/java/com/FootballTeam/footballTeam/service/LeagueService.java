package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.LeagueRequestDto;
import com.FootballTeam.footballTeam.dto.response.LeagueResponseDto;
import com.FootballTeam.footballTeam.model.League;
import com.FootballTeam.footballTeam.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeagueService implements LeagueServiceInterface {

    private final LeagueRepository leagueRepository;

    @Autowired
    public LeagueService(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    // Converte LeagueRequestDto in Entità League
    private League convertToEntity(LeagueRequestDto leagueRequestDto) {
        League league = new League();
        league.setName(leagueRequestDto.getName());
        league.setCountry(leagueRequestDto.getCountry());
        return league;
    }

    // Converte Entità League in LeagueResponseDto
    private LeagueResponseDto convertToDto(League league) {
        return new LeagueResponseDto(league.getId(), league.getName(), league.getCountry());
    }

    @Override
    @Transactional
    public LeagueResponseDto createLeague(LeagueRequestDto leagueRequestDto) {
        League league = convertToEntity(leagueRequestDto);
        League savedLeague = leagueRepository.save(league);
        return convertToDto(savedLeague);
    }

    @Override
    public List<LeagueResponseDto> getAllLeagues() {
        return leagueRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LeagueResponseDto> getLeagueById(Long id) {
        return leagueRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public LeagueResponseDto updateLeague(Long id, LeagueRequestDto leagueRequestDto) {
        League existingLeague = leagueRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Campionato/Coppa non trovato con ID: " + id));

        existingLeague.setName(leagueRequestDto.getName());
        existingLeague.setCountry(leagueRequestDto.getCountry());
        League updatedLeague = leagueRepository.save(existingLeague);
        return convertToDto(updatedLeague);
    }

    @Override
    @Transactional
    public void deleteLeague(Long id) {
        if (!leagueRepository.existsById(id)) {
            throw new NoSuchElementException("Campionato/Coppa non trovato con ID: " + id);
        }

        leagueRepository.deleteById(id);
    }
}