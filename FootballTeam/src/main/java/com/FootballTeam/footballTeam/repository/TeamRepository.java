package com.FootballTeam.footballTeam.repository;

import com.FootballTeam.footballTeam.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);
    List<Team> findByCoach(String coach);
    List<Team> findByFoundingYear(int foundingYear);


}