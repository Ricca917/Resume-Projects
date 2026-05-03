package com.FootballTeam.footballTeam.repository;

import com.FootballTeam.footballTeam.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {
    Optional<League> findByName(String name);
}
