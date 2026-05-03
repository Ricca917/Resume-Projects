-- SQL Script per la Creazione delle Tabelle e Popolamento del Database

-- Disabilita temporaneamente i controlli sulle chiavi esterne per permettere il DROP delle tabelle
SET FOREIGN_KEY_CHECKS = 0;

-- Se le tabelle esistono già, vengono eliminate per avere un DB pulito
DROP TABLE IF EXISTS team_leagues;
DROP TABLE IF EXISTS contracts;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS leagues;
DROP TABLE IF EXISTS users;

-- Creazione delle tabelle

-- Tabella 'leagues'
CREATE TABLE IF NOT EXISTS leagues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    country VARCHAR(255) NOT NULL
);

-- Tabella 'teams'
CREATE TABLE IF NOT EXISTS teams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(255),
    founding_year INT,
    president VARCHAR(255) NOT NULL,
    coach VARCHAR(255) NOT NULL
);

-- Tabella 'players'
CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    age INT NOT NULL,
    position VARCHAR(255) NOT NULL,
    nationality VARCHAR(255) NOT NULL,
    jersey_number INT,
    appearances INT,
    goals INT,
    salary DOUBLE NOT NULL,
    is_free_agent BOOLEAN NOT NULL,
    team_id BIGINT,
    FOREIGN KEY (team_id) REFERENCES teams(id)
);

-- Tabella 'contracts'
CREATE TABLE IF NOT EXISTS contracts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    salary DECIMAL(15, 2) NOT NULL,
    provisions TEXT,
    player_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (player_id) REFERENCES players(id)
);

-- Tabella 'users'
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

-- Tabella di join per la relazione ManyToMany tra 'teams' e 'leagues'
CREATE TABLE IF NOT EXISTS team_leagues (
    team_id BIGINT NOT NULL,
    league_id BIGINT NOT NULL,
    PRIMARY KEY (team_id, league_id),
    FOREIGN KEY (team_id) REFERENCES teams(id),
    FOREIGN KEY (league_id) REFERENCES leagues(id)
);

-- Popolamento delle tabelle

-- Inserimenti nella tabella 'leagues'
INSERT INTO leagues (name, country) VALUES
('Serie A', 'Italy'),
('Premier League', 'Inghilterra');

-- Inserimento nella tabella 'teams'
INSERT INTO teams (team_name, founding_year, president, coach) VALUES
('Juventus', 1897, 'Gianluca Ferrero', 'Igor Tudor'),
('Manchester United', 1878, 'Joel Glazer', 'Arne Slot');

-- Associazione delle squadre alle leghe nella tabella 'team_leagues' (ManyToMany)
INSERT INTO team_leagues (team_id, league_id) VALUES
((SELECT id FROM teams WHERE team_name = 'Juventus'), (SELECT id FROM leagues WHERE name = 'Serie A')),
((SELECT id FROM teams WHERE team_name = 'Manchester United'), (SELECT id FROM leagues WHERE name = 'Premier League'));

-- Inserimento nella tabella 'players' (5 giocatori: 3 associati a squadre, 2 free agent)
INSERT INTO players (first_name, last_name, date_of_birth, age, position, nationality, jersey_number, appearances, goals, salary, is_free_agent, team_id) VALUES
-- Giocatori per Juventus
('Federico', 'Chiesa', '1997-10-25', 27, 'Winger', 'Italian', 7, 120, 35, 5000000.00, FALSE, (SELECT id FROM teams WHERE team_name = 'Juventus')),
('Manuel', 'Locatelli', '1998-01-08', 26, 'Midfielder', 'Italian', 5, 150, 10, 3500000.00, FALSE, (SELECT id FROM teams WHERE team_name = 'Juventus')),
-- Giocatore per Manchester United
('Bruno', 'Fernandes', '1994-09-08', 30, 'Attacking Midfielder', 'Portuguese', 8, 200, 70, 10000000.00, FALSE, (SELECT id FROM teams WHERE team_name = 'Manchester United')),
-- Giocatori Free Agent
('David', 'de Gea', '1990-11-07', 34, 'Goalkeeper', 'Spanish', 1, 400, 0, 0.00, TRUE, NULL),
('Jesse', 'Lingard', '1992-12-15', 32, 'Attacking Midfielder', 'English', 14, 250, 40, 0.00, TRUE, NULL);

-- Inserimento dati nella tabella 'contracts' (Solo per giocatori associati a squadre!)
INSERT INTO contracts (start_date, end_date, salary, provisions, player_id) VALUES
('2023-07-01', '2027-06-30', 5000000.00, 'Bonus legati alle prestazioni e qualificazioni europee.', (SELECT id FROM players WHERE first_name = 'Federico' AND last_name = 'Chiesa')),
('2022-08-01', '2026-06-30', 3500000.00, 'Clausola di rinnovo automatico.', (SELECT id FROM players WHERE first_name = 'Manuel' AND last_name = 'Locatelli')),
('2020-01-29', '2028-06-30', 10000000.00, 'Bonus per gol e assist, clausola rescissoria alta.', (SELECT id FROM players WHERE first_name = 'Bruno' AND last_name = 'Fernandes'));

-- Inserimento di un User con ruolo ADMIN
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$iVG8TT0lgCVxiMiIIjIAHe3OYN9X8LtnA3mCxfCUKlgsvI2iLHHOa', 'ADMIN');

-- Riabilita i controlli sulle chiavi esterne
SET FOREIGN_KEY_CHECKS = 1;