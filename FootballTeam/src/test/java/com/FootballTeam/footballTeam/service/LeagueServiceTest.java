package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.LeagueRequestDto;
import com.FootballTeam.footballTeam.dto.response.LeagueResponseDto;
import com.FootballTeam.footballTeam.model.League;
import com.FootballTeam.footballTeam.repository.LeagueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeagueServiceTest {

    @Mock
    private LeagueRepository leagueRepository;

    @InjectMocks
    private LeagueService leagueService;

    private League league1;
    private League league2;

    @BeforeEach
    void setUp() {
        league1 = new League();
        league1.setId(1L);
        league1.setName("Serie A");
        league1.setCountry("Italia");

        league2 = new League();
        league2.setId(2L);
        league2.setName("Premier League");
        league2.setCountry("Inghilterra");
    }

    @Test
    void testCreateLeague_Success() {
        LeagueRequestDto requestDto = new LeagueRequestDto();
        requestDto.setName("La Liga");
        requestDto.setCountry("Spagna");

        League newLeague = new League();
        newLeague.setId(3L);
        newLeague.setName("La Liga");
        newLeague.setCountry("Spagna");

        when(leagueRepository.save(any(League.class))).thenReturn(newLeague);

        LeagueResponseDto responseDto = leagueService.createLeague(requestDto);

        assertNotNull(responseDto);
        assertEquals(newLeague.getId(), responseDto.getId());
        assertEquals(requestDto.getName(), responseDto.getName());
        assertEquals(requestDto.getCountry(), responseDto.getCountry());

        verify(leagueRepository, times(1)).save(any(League.class));
    }

    @Test
    void testGetAllLeagues_Success() {
        List<League> leagues = Arrays.asList(league1, league2);
        when(leagueRepository.findAll()).thenReturn(leagues);

        List<LeagueResponseDto> result = leagueService.getAllLeagues();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(league1.getId(), result.get(0).getId());
        assertEquals(league2.getId(), result.get(1).getId());

        verify(leagueRepository, times(1)).findAll();
    }

    @Test
    void testGetLeagueById_Found() {
        when(leagueRepository.findById(1L)).thenReturn(Optional.of(league1));

        Optional<LeagueResponseDto> result = leagueService.getLeagueById(1L);

        assertTrue(result.isPresent());
        assertEquals(league1.getId(), result.get().getId());
        assertEquals(league1.getName(), result.get().getName());

        verify(leagueRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLeagueById_NotFound() {
        when(leagueRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<LeagueResponseDto> result = leagueService.getLeagueById(99L);

        assertFalse(result.isPresent());
        verify(leagueRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateLeague_Success() {
        LeagueRequestDto requestDto = new LeagueRequestDto();
        requestDto.setName("Bundesliga Aggiornata");
        requestDto.setCountry("Germany Aggiornata");

        League updatedLeague = new League();
        updatedLeague.setId(league1.getId());
        updatedLeague.setName(requestDto.getName());
        updatedLeague.setCountry(requestDto.getCountry());

        when(leagueRepository.findById(league1.getId())).thenReturn(Optional.of(league1));
        when(leagueRepository.save(any(League.class))).thenReturn(updatedLeague);

        LeagueResponseDto responseDto = leagueService.updateLeague(league1.getId(), requestDto);

        assertNotNull(responseDto);
        assertEquals(league1.getId(), responseDto.getId());
        assertEquals(requestDto.getName(), responseDto.getName());
        assertEquals(requestDto.getCountry(), responseDto.getCountry());

        verify(leagueRepository, times(1)).findById(league1.getId());
        verify(leagueRepository, times(1)).save(league1);
    }

    @Test
    void testUpdateLeague_NotFound() {
        Long nonExistentId = 99L;
        LeagueRequestDto requestDto = new LeagueRequestDto();
        requestDto.setName("Inesistente");
        requestDto.setCountry("Nessuno");

        when(leagueRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            leagueService.updateLeague(nonExistentId, requestDto);
        });
        assertTrue(thrown.getMessage().contains("Campionato/Coppa non trovato con ID: " + nonExistentId));

        verify(leagueRepository, times(1)).findById(nonExistentId);
        verify(leagueRepository, never()).save(any(League.class));
    }

    @Test
    void testDeleteLeague_Success() {
        when(leagueRepository.existsById(league1.getId())).thenReturn(true);
        doNothing().when(leagueRepository).deleteById(league1.getId());

        leagueService.deleteLeague(league1.getId());

        verify(leagueRepository, times(1)).existsById(league1.getId());
        verify(leagueRepository, times(1)).deleteById(league1.getId());
    }

    @Test
    void testDeleteLeague_NotFound() {
        Long nonExistentId = 99L;
        when(leagueRepository.existsById(nonExistentId)).thenReturn(false);

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            leagueService.deleteLeague(nonExistentId);
        });
        assertTrue(thrown.getMessage().contains("Campionato/Coppa non trovato con ID: " + nonExistentId));

        verify(leagueRepository, times(1)).existsById(nonExistentId);
        verify(leagueRepository, never()).deleteById(anyLong());
    }
}