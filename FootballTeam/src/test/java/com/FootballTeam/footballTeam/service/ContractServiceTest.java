package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.ContractRequestDto;
import com.FootballTeam.footballTeam.dto.response.ContractResponseDto;
import com.FootballTeam.footballTeam.model.Contract;
import com.FootballTeam.footballTeam.model.Player;
import com.FootballTeam.footballTeam.model.Team;
import com.FootballTeam.footballTeam.repository.ContractRepository;
import com.FootballTeam.footballTeam.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private ContractService contractService;

    private Player player;
    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team();
        team.setId(1L);
        team.setTeamName("Test Team");

        player = new Player(
                "Lionel",
                "Messi",
                LocalDate.of(1987, 6, 24),
                37,
                "Attaccante",
                "Argentina",
                10,
                800,
                700,
                false,
                team,
                null
        );
        player.setId(1L);
        player.setTeam(team);
        team.addPlayer(player);
    }

    @Test
    void testCreateContract_Success() {
        ContractRequestDto requestDto = new ContractRequestDto();
        requestDto.setStartDate(LocalDate.of(2024, 1, 1));
        requestDto.setEndDate(LocalDate.of(2025, 12, 31));
        requestDto.setSalary(new BigDecimal("1000000.0"));
        requestDto.setProvisions("Bonus Performance");
        requestDto.setPlayerId(player.getId());

        Contract contract = new Contract();
        contract.setId(1L);
        contract.setStartDate(requestDto.getStartDate());
        contract.setEndDate(requestDto.getEndDate());
        contract.setSalary(requestDto.getSalary()); // Usa BigDecimal
        contract.setProvisions(requestDto.getProvisions());
        contract.setPlayer(player);

        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);

        ContractResponseDto responseDto = contractService.createContract(requestDto);

        assertNotNull(responseDto);
        assertEquals(contract.getId(), responseDto.getId());
        assertEquals(requestDto.getStartDate(), responseDto.getStartDate());
        assertEquals(requestDto.getEndDate(), responseDto.getEndDate());
        assertEquals(requestDto.getSalary(), responseDto.getSalary());
        assertEquals(requestDto.getProvisions(), responseDto.getProvisions());
        assertEquals(player.getId(), responseDto.getPlayerId());
        assertEquals(player.getFirstName(), responseDto.getPlayerFirstName());
        assertEquals(player.getLastName(), responseDto.getPlayerLastName());
        assertEquals(team.getId(), responseDto.getTeamId());
        assertEquals(team.getTeamName(), responseDto.getTeamName());

        verify(playerRepository, times(1)).findById(player.getId());
        verify(contractRepository, times(1)).save(any(Contract.class));
    }

    @Test
    void testCreateContract_PlayerNotFound() {
        ContractRequestDto requestDto = new ContractRequestDto();
        requestDto.setPlayerId(99L);
        requestDto.setSalary(new BigDecimal("0.0"));

        when(playerRepository.findById(requestDto.getPlayerId())).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            contractService.createContract(requestDto);
        });
        assertTrue(thrown.getMessage().contains("Player not found with ID: " + requestDto.getPlayerId()));

        verify(playerRepository, times(1)).findById(requestDto.getPlayerId());
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void testCreateContract_PlayerAlreadyHasContract() {
        Contract existingContract = new Contract();
        existingContract.setId(2L);
        player.setContract(existingContract);

        ContractRequestDto requestDto = new ContractRequestDto();
        requestDto.setPlayerId(player.getId());
        requestDto.setSalary(new BigDecimal("0.0"));

        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            contractService.createContract(requestDto);
        });
        assertTrue(thrown.getMessage().contains("Player with ID: " + player.getId() + " already has an active contract."));

        verify(playerRepository, times(1)).findById(player.getId());
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void testGetAllContracts_Success() {
        Contract contract1 = new Contract();
        contract1.setId(1L);
        contract1.setStartDate(LocalDate.of(2024, 1, 1));
        contract1.setEndDate(LocalDate.of(2025, 12, 31));
        contract1.setSalary(new BigDecimal("1000000.0"));
        contract1.setProvisions("Bonus Performance");
        contract1.setPlayer(player); // Associa il giocatore

        Contract contract2 = new Contract();
        contract2.setId(2L);
        contract2.setStartDate(LocalDate.of(2023, 1, 1));
        contract2.setEndDate(LocalDate.of(2024, 12, 31));
        contract2.setSalary(new BigDecimal("500000.0"));

        when(contractRepository.findAll()).thenReturn(List.of(contract1, contract2));

        List<ContractResponseDto> result = contractService.getAllContracts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(contract1.getId(), result.get(0).getId());
        assertEquals(player.getId(), result.get(0).getPlayerId());
        assertNull(result.get(1).getPlayerId());

        verify(contractRepository, times(1)).findAll();
    }

    @Test
    void testGetContractById_Found() {
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setStartDate(LocalDate.of(2024, 1, 1));
        contract.setPlayer(player);
        contract.setSalary(new BigDecimal("100.0"));

        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        Optional<ContractResponseDto> result = contractService.getContractById(1L);

        assertTrue(result.isPresent());
        assertEquals(contract.getId(), result.get().getId());
        assertEquals(player.getId(), result.get().getPlayerId());
        verify(contractRepository, times(1)).findById(1L);
    }

    @Test
    void testGetContractById_NotFound() {

        when(contractRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ContractResponseDto> result = contractService.getContractById(1L);

        assertFalse(result.isPresent());
        verify(contractRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateContract_Success() {

        ContractRequestDto requestDto = new ContractRequestDto();
        requestDto.setStartDate(LocalDate.of(2024, 1, 1));
        requestDto.setEndDate(LocalDate.of(2026, 12, 31));
        requestDto.setSalary(new BigDecimal("1200000.0"));
        requestDto.setProvisions("Nuova Clausola");
        requestDto.setPlayerId(player.getId());

        Contract existingContract = new Contract();
        existingContract.setId(1L);
        existingContract.setStartDate(LocalDate.of(2024, 1, 1));
        existingContract.setEndDate(LocalDate.of(2025, 12, 31));
        existingContract.setSalary(new BigDecimal("1000000.0"));
        existingContract.setProvisions("Vecchia Clausola");
        existingContract.setPlayer(player);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(existingContract));
        when(contractRepository.save(any(Contract.class))).thenReturn(existingContract);

        ContractResponseDto responseDto = contractService.updateContract(1L, requestDto);

        assertNotNull(responseDto);
        assertEquals(requestDto.getEndDate(), responseDto.getEndDate());
        assertEquals(requestDto.getSalary(), responseDto.getSalary());
        assertEquals(requestDto.getProvisions(), responseDto.getProvisions());
        assertEquals(player.getId(), responseDto.getPlayerId());

        verify(contractRepository, times(1)).findById(1L);
        verify(contractRepository, times(1)).save(existingContract);
    }

    @Test
    void testUpdateContract_NotFound() {

        Long contractId = 99L;
        ContractRequestDto requestDto = new ContractRequestDto();
        requestDto.setPlayerId(player.getId());
        requestDto.setSalary(new BigDecimal("0.0"));

        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());


        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            contractService.updateContract(contractId, requestDto);
        });
        assertTrue(thrown.getMessage().contains("Contratto non trovato con ID: " + contractId));

        verify(contractRepository, times(1)).findById(contractId);
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void testUpdateContract_CannotChangePlayer() {
        ContractRequestDto requestDto = new ContractRequestDto();
        requestDto.setPlayerId(99L);
        requestDto.setSalary(new BigDecimal("0.0"));

        Contract existingContract = new Contract();
        existingContract.setId(1L);
        existingContract.setPlayer(player);
        existingContract.setSalary(new BigDecimal("0.0"));

        when(contractRepository.findById(1L)).thenReturn(Optional.of(existingContract));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            contractService.updateContract(1L, requestDto);
        });
        assertTrue(thrown.getMessage().contains("Cannot change the associated player (ID: " + requestDto.getPlayerId() + ") for an existing contract via this update method."));

        verify(contractRepository, times(1)).findById(1L);
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void testDeleteContract_Success() {
        Contract contractToDelete = new Contract();
        contractToDelete.setId(1L);
        contractToDelete.setPlayer(player);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(contractToDelete));

        contractService.deleteContract(1L);

        verify(contractRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(player);
        assertNull(player.getContract());
        verify(contractRepository, times(1)).delete(contractToDelete);
    }

    @Test
    void testDeleteContract_Success_NoAssociatedPlayer() {
        Contract contractToDelete = new Contract();
        contractToDelete.setId(1L);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(contractToDelete));

        contractService.deleteContract(1L);

        verify(contractRepository, times(1)).findById(1L);
        verify(playerRepository, never()).save(any(Player.class));
        verify(contractRepository, times(1)).delete(contractToDelete);
    }

    @Test
    void testDeleteContract_NotFound() {

        Long contractId = 99L;
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            contractService.deleteContract(contractId);
        });
        assertTrue(thrown.getMessage().contains("Contratto non trovato con ID: " + contractId));

        verify(contractRepository, times(1)).findById(contractId);
        verify(contractRepository, never()).delete(any(Contract.class));
        verify(playerRepository, never()).save(any(Player.class));
    }
}