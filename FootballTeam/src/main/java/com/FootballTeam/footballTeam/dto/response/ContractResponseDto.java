package com.FootballTeam.footballTeam.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContractResponseDto {
    private final Long id;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal salary;
    private final String provisions;
    private final Long playerId;
    private final String playerFirstName;
    private final String playerLastName;
    private final Long teamId;
    private final String teamName;

    // Costruttore con gli attributi
    public ContractResponseDto(Long id, LocalDate startDate, LocalDate endDate, BigDecimal salary, String provisions, Long playerId, String playerFirstName, String playerLastName, Long teamId, String teamName) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.salary = salary;
        this.provisions = provisions;
        this.playerId = playerId;
        this.playerFirstName = playerFirstName;
        this.playerLastName = playerLastName;
        this.teamId = teamId;
        this.teamName = teamName;
    }

    // Getter
    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getProvisions() {
        return provisions;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public String getPlayerFirstName() {
        return playerFirstName;
    }

    public String getPlayerLastName() {
        return playerLastName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }
}