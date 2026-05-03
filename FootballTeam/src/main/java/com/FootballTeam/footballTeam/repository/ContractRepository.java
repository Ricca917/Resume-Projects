package com.FootballTeam.footballTeam.repository;

import com.FootballTeam.footballTeam.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

}
