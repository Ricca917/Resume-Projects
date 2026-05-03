package it.itsprodigi.fsd2;

import it.itsprodigi.fsd2.engine.Combattimento;
import it.itsprodigi.fsd2.model.*;
import it.itsprodigi.fsd2.ui.TerminalUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point del gioco.
 * Gestisce la schermata di creazione personaggio e la composizione del party.
 *
 * Per avviare dal terminale di VS Code:
 *   cd Personaggio
 *   mvn compile exec:java
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        TerminalUI.titolo();

        // ── Selezione classe ───────────────────────────────────────────────
        System.out.println(TerminalUI.BOLD + "  === CREAZIONE PERSONAGGIO ===" + TerminalUI.RESET);
        System.out.println();
        System.out.printf("  %s[1] Guerriero%s  (Umano  | HP: ~125 | Bonus: Forza +4)%n",
                TerminalUI.YELLOW, TerminalUI.RESET);
        System.out.printf("      Abilita' speciale: %sColpo Devastante%s — tripla forza, una volta%n",
                TerminalUI.PURPLE, TerminalUI.RESET);
        System.out.println();
        System.out.printf("  %s[2] Mago%s       (Elfo   | HP: ~88  | Bonus: Intelligenza +5)%n",
                TerminalUI.CYAN, TerminalUI.RESET);
        System.out.printf("      Abilita' speciale: %sPalla di Fuoco%s — doppia magia + bonus, una volta%n",
                TerminalUI.PURPLE, TerminalUI.RESET);
        System.out.println();
        System.out.printf("  %s[3] Chierico%s   (Nano   | HP: ~110 | Bonus: Saggezza +4)%n",
                TerminalUI.GREEN, TerminalUI.RESET);
        System.out.printf("      Abilita' speciale: %sGuarigione di Massa%s — cura tutto il party, una volta%n",
                TerminalUI.PURPLE, TerminalUI.RESET);
        System.out.println();
        System.out.print("  Scelta classe > ");
        String sceltaClasse = scanner.nextLine().trim();

        System.out.print("  Nome del tuo personaggio > ");
        String nomeEroe = scanner.nextLine().trim();
        if (nomeEroe.isBlank()) nomeEroe = "Senza Nome";

        // Creazione protagonista in base alla scelta
        Personaggio protagonista = switch (sceltaClasse) {
            case "1" -> new Guerriero(nomeEroe);
            case "2" -> new Mago(nomeEroe);
            case "3" -> new Chierico(nomeEroe);
            default  -> {
                System.out.println("  Scelta non valida — creato Guerriero.");
                yield new Guerriero(nomeEroe);
            }
        };

        // ── Composizione party ─────────────────────────────────────────────
        // Il party ha sempre 3 membri. I compagni complementano la classe del giocatore.
        List<Personaggio> party = new ArrayList<>();
        party.add(protagonista);

        // Aggiungiamo compagni di classi diverse da quella scelta
        if (!(protagonista instanceof Mago))     party.add(new Mago("Aldric"));
        if (!(protagonista instanceof Chierico)) party.add(new Chierico("Seraphina"));
        if (party.size() < 3)                   party.add(new Guerriero("Thordak"));

        // Passa il riferimento al party al Chierico per Guarigione di Massa
        for (Personaggio p : party) {
            if (p instanceof Chierico c) c.setParty(party);
        }

        // ── Creazione Boss ─────────────────────────────────────────────────
        TerminalUI.separatore();
        System.out.println(TerminalUI.BOLD + "  === IL NEMICO ===" + TerminalUI.RESET);
        System.out.print("  Nome del Boss > ");
        String nomeBoss = scanner.nextLine().trim();
        if (nomeBoss.isBlank()) nomeBoss = "Malachar";

        System.out.print("  Titolo del Boss > ");
        String titoloBoss = scanner.nextLine().trim();
        if (titoloBoss.isBlank()) titoloBoss = "Il Flagello";

        Boss boss = new Boss(nomeBoss, titoloBoss);

        // ── Riepilogo pre-battaglia ────────────────────────────────────────
        TerminalUI.separatore();
        System.out.println(TerminalUI.BOLD + "  === IL TUO PARTY ===" + TerminalUI.RESET);
        for (Personaggio p : party) {
            System.out.printf("  %-12s %-10s  HP: %3d  FOR: %2d  INT: %2d  SAG: %2d%n",
                    p.getNome(),
                    "(" + p.getClasse() + ")",
                    p.getVitaMax(),
                    p.getForza(),
                    p.getIntelligenza(),
                    p.getSaggezza());
        }
        System.out.println();
        System.out.printf("  Nemico: %s%s \"%s\"%s  HP: %d  FOR: %d%n",
                TerminalUI.RED + TerminalUI.BOLD,
                boss.getNome(), boss.getTitolo(),
                TerminalUI.RESET,
                boss.getVitaMax(),
                boss.getForza());
        TerminalUI.separatore();

        System.out.println();
        System.out.print("  Premi INVIO per iniziare il combattimento...");
        scanner.nextLine();

        // ── Avvio combattimento ────────────────────────────────────────────
        Combattimento combattimento = new Combattimento(party, boss, scanner);
        combattimento.avvia();

        scanner.close();
    }
}
