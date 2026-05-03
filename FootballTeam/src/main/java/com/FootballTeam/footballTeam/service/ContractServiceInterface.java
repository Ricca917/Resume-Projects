package com.FootballTeam.footballTeam.service;

import com.FootballTeam.footballTeam.dto.request.ContractRequestDto;
import com.FootballTeam.footballTeam.dto.response.ContractResponseDto;

import java.util.List;
import java.util.Optional;

public interface ContractServiceInterface {
    ContractResponseDto createContract(ContractRequestDto contractRequestDto);
    List<ContractResponseDto> getAllContracts();
    Optional<ContractResponseDto> getContractById(Long id);
    ContractResponseDto updateContract(Long id, ContractRequestDto contractRequestDto);
    void deleteContract(Long id);

}