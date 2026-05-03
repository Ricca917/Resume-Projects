package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.ContractRequestDto;
import com.FootballTeam.footballTeam.dto.response.ContractResponseDto;
import com.FootballTeam.footballTeam.model.Contract;
import com.FootballTeam.footballTeam.model.Player;
import com.FootballTeam.footballTeam.repository.ContractRepository;
import com.FootballTeam.footballTeam.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractService implements ContractServiceInterface {

    private final ContractRepository contractRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public ContractService(ContractRepository contractRepository, PlayerRepository playerRepository) {
        this.contractRepository = contractRepository;
        this.playerRepository = playerRepository;
    }

    // Converte ContractRequestDto in Entità Contract
    private Contract convertToEntity(ContractRequestDto contractRequestDto) {
        Contract contract = new Contract();
        contract.setStartDate(contractRequestDto.getStartDate());
        contract.setEndDate(contractRequestDto.getEndDate());
        contract.setSalary(contractRequestDto.getSalary());
        contract.setProvisions(contractRequestDto.getProvisions());
        return contract;
    }

    // Converte Entità Contract in ContractResponseDto
    private ContractResponseDto convertToDto(Contract contract) {
        Long playerId = null;
        String playerFirstName = null;
        String playerLastName = null;
        Long teamId = null;
        String teamName = null;

        if (contract.getPlayer() != null) {
            playerId = contract.getPlayer().getId();
            playerFirstName = contract.getPlayer().getFirstName();
            playerLastName = contract.getPlayer().getLastName();
            if (contract.getPlayer().getTeam() != null) {
                teamId = contract.getPlayer().getTeam().getId();
                teamName = contract.getPlayer().getTeam().getTeamName();
            }
        }

        String provisions = contract.getProvisions();

        return new ContractResponseDto(
                contract.getId(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getSalary(),
                provisions,
                playerId,
                playerFirstName,
                playerLastName,
                teamId,
                teamName
        );
    }

    @Override
    @Transactional
    public ContractResponseDto createContract(ContractRequestDto contractRequestDto) {
        Player player = playerRepository.findById(contractRequestDto.getPlayerId())
                .orElseThrow(() -> new NoSuchElementException("Player not found with ID: " + contractRequestDto.getPlayerId()));

        if (player.getContract() != null) {
            throw new IllegalArgumentException("Player with ID: " + player.getId() + " already has an active contract.");
        }

        Contract contract = convertToEntity(contractRequestDto);
        contract.setPlayer(player);

        Contract savedContract = contractRepository.save(contract);

        return convertToDto(savedContract);
    }

    @Override
    public List<ContractResponseDto> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ContractResponseDto> getContractById(Long id) {
        return contractRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public ContractResponseDto updateContract(Long id, ContractRequestDto contractRequestDto) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contratto non trovato con ID: " + id));

        if (!existingContract.getPlayer().getId().equals(contractRequestDto.getPlayerId())) {
            throw new IllegalArgumentException("Cannot change the associated player (ID: " + contractRequestDto.getPlayerId() + ") for an existing contract via this update method.");
        }

        existingContract.setStartDate(contractRequestDto.getStartDate());
        existingContract.setEndDate(contractRequestDto.getEndDate());
        existingContract.setSalary(contractRequestDto.getSalary());
        existingContract.setProvisions(contractRequestDto.getProvisions());
        Contract updatedContract = contractRepository.save(existingContract);
        return convertToDto(updatedContract);
    }

    @Override
    @Transactional
    public void deleteContract(Long id) {
        Contract contractToDelete = contractRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contratto non trovato con ID: " + id));

        Player associatedPlayer = contractToDelete.getPlayer();
        if (associatedPlayer != null) {
            associatedPlayer.setContract(null);
            playerRepository.save(associatedPlayer);
        }

        contractRepository.delete(contractToDelete);
    }
}