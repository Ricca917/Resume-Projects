package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.PlayerRequestDto;
import com.FootballTeam.footballTeam.dto.response.PlayerResponseDto;
import com.FootballTeam.footballTeam.model.Player;
import com.FootballTeam.footballTeam.model.Team;
import com.FootballTeam.footballTeam.repository.PlayerRepository;
import com.FootballTeam.footballTeam.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private PlayerService playerService;


    private Player player1;
    private Player player2;
    private PlayerRequestDto playerRequestDto;
    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team("Squadra A", 1990, "Presidente A", "Allenatore A");
        team.setId(1L);

        player1 = new Player("Lionel", "Messi", LocalDate.of(1987, 6, 24), 37, "Attaccante", "Argentina", 10, 800, 700, false, null, null);
        player1.setId(1L);
        player1.setTeam(team);

        player2 = new Player("Cristiano", "Ronaldo", LocalDate.of(1985, 2, 5), 39, "Attaccante", "Portoghese", 7, 900, 750, false, null, null);
        player2.setId(2L);
        player2.setTeam(team);

        playerRequestDto = new PlayerRequestDto("Neymar", "Jr", "Brasil", 32, LocalDate.of(1992, 2, 5), "Attaccante", 11, 400, 300, false, team.getId()
        );
    }

    @Test
    void testCreatePlayer_Success() {
        when(teamRepository.findById(playerRequestDto.getTeamId())).thenReturn(Optional.of(team));
        Player newPlayer = new Player(
                playerRequestDto.getFirstName(), playerRequestDto.getLastName(), playerRequestDto.getDateOfBirth(),
                playerRequestDto.getAge(), playerRequestDto.getPosition(), playerRequestDto.getNationality(),
                playerRequestDto.getJerseyNumber(), playerRequestDto.getAppearances(), playerRequestDto.getGoals(),
                playerRequestDto.getIsFreeAgent(), null, null
        );
        newPlayer.setTeam(team);

        when(playerRepository.save(any(Player.class))).thenReturn(newPlayer);

        PlayerResponseDto result = playerService.createPlayer(playerRequestDto);

        assertNotNull(result);
        assertEquals(playerRequestDto.getFirstName(), result.getFirstName());
        assertEquals(playerRequestDto.getLastName(), result.getLastName());
        assertEquals(playerRequestDto.getTeamId(), result.getTeamId());
        verify(teamRepository, times(1)).findById(playerRequestDto.getTeamId());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testCreatePlayer_TeamNotFound() {
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> playerService.createPlayer(playerRequestDto));

        assertEquals("Team with ID " + playerRequestDto.getTeamId() + " not found.", thrown.getMessage());
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void testGetPlayerById_Found() {
        when(playerRepository.findById(player1.getId())).thenReturn(Optional.of(player1));

        Optional<PlayerResponseDto> result = playerService.getPlayerById(player1.getId());

        assertTrue(result.isPresent());
        assertEquals(player1.getFirstName(), result.get().getFirstName());
        assertEquals(player1.getLastName(), result.get().getLastName());
    }

    @Test
    void testGetPlayerById_NotFound() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<PlayerResponseDto> result = playerService.getPlayerById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllPlayers() {
        when(playerRepository.findAll()).thenReturn(Arrays.asList(player1, player2));

        List<PlayerResponseDto> result = playerService.getAllPlayers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(player1.getFirstName(), result.get(0).getFirstName());
        assertEquals(player2.getFirstName(), result.get(1).getFirstName());
    }

    @Test
    void testUpdatePlayer_SuccessWithTeamChange() {
        Team newTeam = new Team("Squadra B", 2000, "Presidente B", "Allenatore B");
        newTeam.setId(2L);

        PlayerRequestDto updateRequestDto = new PlayerRequestDto(
                "Lionel", "Messi", "Argentina", 37, LocalDate.of(1987, 6, 24), "Centrocampista", 10, 800, 705, false, newTeam.getId()
        );

        when(playerRepository.findById(player1.getId())).thenReturn(Optional.of(player1));
        when(teamRepository.findById(newTeam.getId())).thenReturn(Optional.of(newTeam));
        when(playerRepository.save(any(Player.class))).thenReturn(player1);

        PlayerResponseDto result = playerService.updatePlayer(player1.getId(), updateRequestDto);

        assertNotNull(result);
        assertEquals("Centrocampista", result.getPosition());
        assertEquals(705, result.getGoals());
        assertEquals(newTeam.getId(), result.getTeamId());
        assertEquals(newTeam.getTeamName(), result.getTeamName());

        verify(playerRepository, times(1)).findById(player1.getId());
        verify(teamRepository, times(1)).findById(newTeam.getId());
        verify(playerRepository, times(1)).save(player1);
    }

    @Test
    void testUpdatePlayer_SuccessNoTeamChange() {
        PlayerRequestDto updateRequestDto = new PlayerRequestDto(
                "Lionel", "Messi", "Argentina", 37, LocalDate.of(1987, 6, 24), "Centrocampista", 10, 800, 705, false, player1.getTeam().getId()
        );

        when(playerRepository.findById(player1.getId())).thenReturn(Optional.of(player1));
        when(teamRepository.findById(player1.getTeam().getId())).thenReturn(Optional.of(player1.getTeam()));
        when(playerRepository.save(any(Player.class))).thenReturn(player1);

        // Act
        PlayerResponseDto result = playerService.updatePlayer(player1.getId(), updateRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("Centrocampista", result.getPosition());
        assertEquals(player1.getTeam().getId(), result.getTeamId());
        verify(playerRepository, times(1)).findById(player1.getId());
        verify(teamRepository, times(1)).findById(player1.getTeam().getId());
        verify(playerRepository, times(1)).save(player1);
    }

    @Test
    void testUpdatePlayer_SetToFreeAgent() {
        // Arrange
        // CORREZIONE: Ordine e tipi dei parametri per PlayerRequestDto
        PlayerRequestDto updateRequestDto = new PlayerRequestDto(
                "Lionel", "Messi", "Argentina", 37, LocalDate.of(1987, 6, 24), "Attaccante", 10, 800, 700, true, null
        );

        when(playerRepository.findById(player1.getId())).thenReturn(Optional.of(player1));
        when(playerRepository.save(any(Player.class))).thenReturn(player1);

        // Act
        PlayerResponseDto result = playerService.updatePlayer(player1.getId(), updateRequestDto);

        // Assert
        assertNotNull(result);
        assertTrue(result.getFreeAgent());
        assertNull(result.getTeamId());
        assertNull(result.getTeamName());

        verify(playerRepository, times(1)).findById(player1.getId());
        verify(teamRepository, never()).findById(anyLong());
        verify(playerRepository, times(1)).save(player1);
    }


    @Test
    void testUpdatePlayer_PlayerNotFound() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> playerService.updatePlayer(999L, playerRequestDto));

        verify(playerRepository, never()).save(any(Player.class));
        verify(teamRepository, never()).findById(anyLong());
    }

    @Test
    void testUpdatePlayer_NewTeamNotFound() {
        when(playerRepository.findById(player1.getId())).thenReturn(Optional.of(player1));
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> playerService.updatePlayer(player1.getId(), playerRequestDto));

        verify(playerRepository, never()).save(any(Player.class));
    }


    @Test
    void testDeletePlayer_Success() {
        when(playerRepository.findById(player1.getId())).thenReturn(Optional.of(player1));
        doNothing().when(playerRepository).deleteById(player1.getId());

        playerService.deletePlayer(player1.getId());

        verify(playerRepository, times(1)).findById(player1.getId());
        verify(playerRepository, times(1)).deleteById(player1.getId());
    }

    @Test
    void testDeletePlayer_NotFound() {
        when(playerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> playerService.deletePlayer(999L));

        verify(playerRepository, never()).existsById(anyLong());
        verify(playerRepository, never()).deleteById(anyLong());
    }
}