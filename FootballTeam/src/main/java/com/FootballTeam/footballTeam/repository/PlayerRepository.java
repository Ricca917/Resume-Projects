package com.FootballTeam.footballTeam.repository;
import com.FootballTeam.footballTeam.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeamId(Long teamId);
    List<Player> findByPosition(String position);
    List<Player> findByFirstName(String firstName);
    List<Player> findByLastName(String lastName);
    List<Player> findByNationality(String nationality);
    List<Player> findByIsFreeAgentTrue();

}
