# CLI Fantasy Combat

Gioco di combattimento a turni testuale in Java. Il giocatore crea il proprio personaggio, compone un party di tre eroi e affronta un Boss personalizzabile in uno scontro a turni con meccaniche ispirate a D&D.

## Stack tecnologico

- **Java 21**
- **Maven** (exec-maven-plugin + maven-shade-plugin per fat-jar)

## Prerequisiti

- Java 21+
- Maven 3.x

---

## Avvio

### Dal terminale (consigliato)

```bash
mvn compile exec:java
```

### Fat-JAR eseguibile

```bash
mvn package
java -jar target/cli-fantasy-combat-1.0-SNAPSHOT.jar
```

---

## Come si gioca

All'avvio il gioco guida il giocatore attraverso tre fasi:

1. **Creazione personaggio** — scegli la classe e assegna un nome all'eroe
2. **Definizione del Boss** — scegli il nome e il titolo del nemico finale
3. **Combattimento** — scontro a turni fino alla vittoria o alla sconfitta

### Azioni disponibili ogni turno

| Tasto | Azione            | Effetto                                              |
|-------|-------------------|------------------------------------------------------|
| `1`   | Attacca           | Infligge danni fisici o magici al Boss               |
| `2`   | Abilità speciale  | Abilità unica per classe, usabile una sola volta     |
| `3`   | Difendi           | Dimezza il prossimo danno subito                     |
| `4`   | Fuggi             | Termina la partita                                   |

---

## Classi giocabili

| Classe    | Razza  | HP base | Bonus stat       | Abilità speciale                              |
|-----------|--------|---------|------------------|-----------------------------------------------|
| Guerriero | Umano  | ~125    | Forza +4         | **Colpo Devastante** — tripla forza, una volta |
| Mago      | Elfo   | ~88     | Intelligenza +5  | **Palla di Fuoco** — doppia magia + bonus, una volta |
| Chierico  | Nano   | ~110    | Saggezza +4      | **Guarigione di Massa** — cura tutto il party, una volta |

Le statistiche base (Forza, Destrezza, Costituzione, Intelligenza, Saggezza) vengono generate casualmente nel range **5–18** a ogni nuova partita (meccanica D&D).

---

## Il Boss

Il Boss è un Demone con statistiche potenziate (Forza +6, Intelligenza +4, HP: 200).

- Ogni turno attacca un eroe casuale vivo
- **Ogni 3 turni** usa **Attacco Brutale** (Forza + Intelligenza combinati)

---

## Party

Il party è composto da **3 eroi**. I compagni vengono aggiunti automaticamente con classi diverse da quella del giocatore:

- Se il giocatore non è Mago → viene aggiunto **Aldric** il Mago
- Se il giocatore non è Chierico → viene aggiunta **Seraphina** il Chierico
- Se necessario → viene aggiunto **Thordak** il Guerriero

---

## Struttura del progetto

```
src/main/java/it/itsprodigi/fsd2/
├── Main.java              # Entry point: creazione personaggio, party e boss
├── engine/
│   └── Combattimento.java # Motore del loop a turni
├── model/
│   ├── Personaggio.java   # Classe astratta base (stats D&D, difesa, danno)
│   ├── Guerriero.java
│   ├── Mago.java
│   ├── Chierico.java
│   └── Boss.java
└── ui/
    └── TerminalUI.java    # Output colorato, barre HP, messaggi di gioco
```
