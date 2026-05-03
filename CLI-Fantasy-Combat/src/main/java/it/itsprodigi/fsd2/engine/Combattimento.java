package it.itsprodigi.fsd2.engine;

import it.itsprodigi.fsd2.model.Boss;
import it.itsprodigi.fsd2.model.Chierico;
import it.itsprodigi.fsd2.model.Personaggio;
import it.itsprodigi.fsd2.ui.TerminalUI;

import java.util.List;
import java.util.Scanner;

/**
 * Motore del combattimento a turni.
 *
 * Struttura di ogni turno:
 *   1. Mostra lo stato (barre HP di boss e party)
 *   2. Per ogni eroe vivo: mostra il menu azione e gestisce la scelta
 *   3. Il boss esegue il suo turno (attacco normale o Attacco Brutale ogni 3 turni)
 *   4. Controlla condizioni di vittoria/sconfitta
 */
public class Combattimento {

    private final List<Personaggio> party;
    private final Boss boss;
    private final Scanner scanner;
    private int turno = 0;

    public Combattimento(List<Personaggio> party, Boss boss, Scanner scanner) {
        this.party   = party;
        this.boss    = boss;
        this.scanner = scanner;
    }

    // ── Loop principale ───────────────────────────────────────────────────────
    public void avvia() {
        TerminalUI.separatoreGrosso();
        System.out.printf("%s  ⚔  SCONTRO: Il party affronta %s \"%s\"%s%n",
                TerminalUI.BOLD + TerminalUI.RED,
                boss.getNome(), boss.getTitolo(),
                TerminalUI.RESET);
        TerminalUI.separatoreGrosso();

        while (boss.isVivo() && partyVivo()) {
            turno++;
            System.out.printf("%n%s═══ TURNO %d ═══%s%n",
                    TerminalUI.BOLD + TerminalUI.CYAN, turno, TerminalUI.RESET);

            mostraStato();

            // ── Fase eroi ──────────────────────────────────────────────────
            for (Personaggio eroe : party) {
                if (!eroe.isVivo()) continue;
                if (!boss.isVivo()) break;
                eseguiAzioneEroe(eroe);
            }

            if (!boss.isVivo()) break;

            // ── Fase boss ──────────────────────────────────────────────────
            System.out.println();
            if (turno % 3 == 0) {
                TerminalUI.logBossAbilita(boss.getNome(), boss.getAbilitaSpecialeNome());
            } else {
                System.out.printf("%s  --- Turno del Boss ---%s%n",
                        TerminalUI.RED, TerminalUI.RESET);
            }
            boss.eseguiTurno(party, turno);
        }

        // ── Esito finale ───────────────────────────────────────────────────
        if (!boss.isVivo()) {
            TerminalUI.vittoria();
        } else {
            TerminalUI.sconfitta();
        }
    }

    // ── Visualizzazione stato ─────────────────────────────────────────────────
    private void mostraStato() {
        System.out.println();
        // Boss
        System.out.printf("  %s%-20s%s %s%n",
                TerminalUI.BOLD + TerminalUI.RED,
                boss.getNome() + " [Boss]",
                TerminalUI.RESET,
                TerminalUI.barraHP(boss.getVitaAttuale(), boss.getVitaMax()));
        System.out.println();
        // Party
        for (Personaggio p : party) {
            String statoMorte = p.isVivo() ? "" : TerminalUI.RED + " [MORTO]" + TerminalUI.RESET;
            String difesa     = p.isDifendendo() ? TerminalUI.BLUE + " [DIFESA]" + TerminalUI.RESET : "";
            System.out.printf("  %s%-20s%s %s%s%s%n",
                    TerminalUI.BOLD,
                    p.getNome() + " (" + p.getClasse() + ")",
                    TerminalUI.RESET,
                    TerminalUI.barraHP(p.getVitaAttuale(), p.getVitaMax()),
                    statoMorte, difesa);
        }
        System.out.println();
    }

    // ── Azione eroe: menu + dispatch ─────────────────────────────────────────
    private void eseguiAzioneEroe(Personaggio eroe) {
        System.out.printf("%s  Turno di %s (%s):%s%n",
                TerminalUI.BOLD, eroe.getNome(), eroe.getClasse(), TerminalUI.RESET);
        System.out.println("    [1] Attacca");

        // Opzione 2: abilita' speciale (grigiata se gia' usata)
        if (eroe.haUsatoAbilita()) {
            System.out.printf("    [2] %s (gia' usata)%n", eroe.getAbilitaSpecialeNome());
        } else {
            System.out.printf("    [2] Abilita' Speciale: %s%n", eroe.getAbilitaSpecialeNome());
        }

        System.out.println("    [3] Difendi  (dimezza il prossimo danno subito)");
        System.out.println("    [4] Fuggi");
        System.out.print("    Scelta > ");

        String input = leggiInput();
        System.out.println();

        switch (input) {
            case "1" -> {
                eroe.attacco(boss);
            }
            case "2" -> {
                if (eroe.haUsatoAbilita()) {
                    System.out.println("    Abilita' gia' usata! Attacco normale.");
                    eroe.attacco(boss);
                } else {
                    TerminalUI.logAbilita(eroe.getNome(), eroe.getAbilitaSpecialeNome());
                    // Il Chierico ha bisogno del boss come bersaglio formale,
                    // ma la sua abilita' cura il party (vedi Chierico.abilitaSpeciale)
                    eroe.abilitaSpeciale(boss);
                }
            }
            case "3" -> {
                eroe.setDifendendo(true);
                TerminalUI.logDifesa(eroe.getNome());
            }
            case "4" -> {
                TerminalUI.fuga();
                System.exit(0);
            }
            default -> {
                System.out.println("    Scelta non valida — attacco normale.");
                eroe.attacco(boss);
            }
        }

        if (!boss.isVivo()) {
            System.out.printf("%s  Il Boss %s e' stato sconfitto!%s%n",
                    TerminalUI.BOLD + TerminalUI.GREEN, boss.getNome(), TerminalUI.RESET);
        }
    }

    // ── Utilita' ─────────────────────────────────────────────────────────────
    private boolean partyVivo() {
        return party.stream().anyMatch(Personaggio::isVivo);
    }

    /** Legge l'input ignorando righe vuote. */
    private String leggiInput() {
        String input = "";
        while (input.isBlank()) {
            if (scanner.hasNextLine()) {
                input = scanner.nextLine().trim();
            } else {
                break;
            }
        }
        return input;
    }
}
