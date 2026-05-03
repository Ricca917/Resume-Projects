package com.FootballTeam.footballTeam.controller;

import com.FootballTeam.footballTeam.dto.request.ContractRequestDto;
import com.FootballTeam.footballTeam.dto.response.ContractResponseDto;
import com.FootballTeam.footballTeam.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    // Endpoint per creare un nuovo contratto
    // POST /api/contracts
    @PostMapping
    public ResponseEntity<ContractResponseDto> createContract(@Valid @RequestBody ContractRequestDto contractRequestDto) {
        ContractResponseDto createdContract = contractService.createContract(contractRequestDto);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    // Endpoint per ottenere tutti i contratti
    // GET /api/contracts
    @GetMapping
    public ResponseEntity<List<ContractResponseDto>> getAllContracts() {
        List<ContractResponseDto> contracts = contractService.getAllContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    // Endpoint per ottenere un contratto tramite ID
    // GET /api/contracts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ContractResponseDto> getContractById(@PathVariable Long id) {

        ContractResponseDto contractDto = contractService.getContractById(id)
                .orElseThrow(() -> new NoSuchElementException("Contract not found with ID: " + id));
        return new ResponseEntity<>(contractDto, HttpStatus.OK);
    }

    // Endpoint per aggiornare un contratto esistente
    // PUT /api/contracts/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ContractResponseDto> updateContract(@PathVariable Long id, @Valid @RequestBody ContractRequestDto contractRequestDto) {
        ContractResponseDto updatedContract = contractService.updateContract(id, contractRequestDto);
        return new ResponseEntity<>(updatedContract, HttpStatus.OK);
    }

    // Endpoint per eliminare un contratto
    // DELETE /api/contracts/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}