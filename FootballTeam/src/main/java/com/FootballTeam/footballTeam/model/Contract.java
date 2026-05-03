package com.FootballTeam.footballTeam.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal salary;

    @Column(name = "provisions", columnDefinition = "TEXT")
    private String provisions;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", unique = true, nullable = false)
    private Player player;

    // Costruttore vuoto
    public Contract() {
    }

    // Costruttore con Attributi
    public Contract(LocalDate startDate, LocalDate endDate, BigDecimal salary, String provisions, Player player) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.salary = salary;
        this.provisions = provisions;
        this.player = player;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    // Metodo ToString per le Info sul Contratto
    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", salary=" + salary +
                ", provisions='" + provisions + '\'' +
                ", playerId=" + (player != null ? player.getId() : "null") +
                '}';
    }
}