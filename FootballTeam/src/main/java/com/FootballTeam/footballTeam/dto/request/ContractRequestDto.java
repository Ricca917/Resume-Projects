package com.FootballTeam.footballTeam.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ContractRequestDto {

    @NotNull(message = "La data di inizio contratto non può essere nulla")
    private LocalDate startDate;

    @NotNull(message = "La data di fine contratto non può essere nulla")
    @FutureOrPresent(message = "La data di fine deve essere nel presente o nel futuro")
    private LocalDate endDate;

    @NotNull(message = "Lo stipendio non può essere nullo")
    @DecimalMin(value = "0.01", message = "Lo stipendio minimo deve essere un valore positivo")
    private BigDecimal salary;

    private String provisions;

    @NotNull(message = "L'ID del giocatore non può essere nullo")
    private Long playerId;

    // Costruttore vuoto
    public ContractRequestDto() {
    }

    // Costruttore con Attributi
    public ContractRequestDto(LocalDate startDate, LocalDate endDate, BigDecimal salary, String provisions, Long playerId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.salary = salary;
        this.provisions = provisions;
        this.playerId = playerId;
    }

    // Getter e Setter

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getProvisions() {
        return provisions;
    }

    public void setProvisions(String provisions) {
        this.provisions = provisions;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}